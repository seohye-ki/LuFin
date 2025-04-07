import axiosInstance from '../axios';

export type FolderType = 'classrooms' | 'missions';

interface PresignedUrlResponse {
  uploadUrl: string;
  key: string;
}

interface ApiResponse<T> {
  isSuccess: boolean;
  data?: T;
  code?: string;
  message?: string;
}

/**
 * 파일 업로드 관련 유틸리티 함수들
 */
export const fileService = {
  /**
   * Presigned URL 요청
   * @param folder 업로드할 폴더 ('classrooms' | 'missions')
   * @param file 업로드할 파일
   * @returns Presigned URL 응답 (uploadUrl, key)
   */
  getPresignedUrl: async (folder: FolderType, file: File): Promise<PresignedUrlResponse> => {
    console.log('[FileService] Getting presigned URL:', { folder, fileName: file.name });
    const response = await axiosInstance.get<ApiResponse<PresignedUrlResponse> | PresignedUrlResponse>(
      `/files/presigned-url?folder=${folder}&filename=${encodeURIComponent(file.name)}`,
    );

    console.log('[FileService] Raw API response:', response.data);

    // response.data가 직접 PresignedUrlResponse 형태인 경우를 처리
    if ('uploadUrl' in response.data && 'key' in response.data) {
      return response.data as PresignedUrlResponse;
    }

    // ApiResponse 형태로 왔을 때 처리
    if (!response.data.isSuccess || !response.data.data) {
      console.error('[FileService] Failed to get presigned URL:', response.data);
      throw new Error(response.data.message || '파일 업로드 URL을 가져오는데 실패했습니다.');
    }

    return response.data.data;
  },

  /**
   * 이미지 다운로드 URL 가져오기
   * @param key 이미지 키
   * @returns 다운로드 URL
   */
  getImageUrl: async (key: string | null): Promise<string> => {
    if (!key) return '';
    
    try {
      const folder = key.split('/')[0] as FolderType;
      const filename = key.split('/').pop() || '';
      
      const response = await axiosInstance.get<ApiResponse<string>>(
        `/files/download-url?folder=${folder}&filename=${encodeURIComponent(filename)}`
      );

      if (!response.data.isSuccess || !response.data.data) {
        throw new Error('Failed to get download URL');
      }

      return response.data.data;
    } catch (error) {
      console.error('[FileService] Failed to get image URL:', error);
      return '';
    }
  },

  /**
   * S3에 파일 업로드
   * @param uploadUrl Presigned URL
   * @param file 업로드할 파일
   */
  uploadToS3: async (uploadUrl: string, file: File): Promise<void> => {
    console.log('[FileService] Uploading to S3:', { uploadUrl });
    try {
      const response = await fetch(uploadUrl, {
        method: 'PUT',
        body: file,
        headers: {
          'Content-Type': file.type,
        },
        mode: 'cors',  // 명시적으로 CORS 모드 설정
        credentials: 'omit',  // S3는 credentials이 필요없음
      });
      
      if (!response.ok) {
        console.error('[FileService] S3 upload failed:', { 
          status: response.status, 
          statusText: response.statusText 
        });
        throw new Error(`S3 upload failed: ${response.statusText}`);
      }
      
      console.log('[FileService] S3 upload response:', { status: response.status });
    } catch (error) {
      console.error('[FileService] S3 upload error:', error);
      throw error;
    }
  },

  /**
   * 파일 확장자 검증
   * @param file 검증할 파일
   * @returns 유효한 확장자인지 여부
   */
  isValidFileExtension: (file: File): boolean => {
    const allowedExtensions = ['jpg', 'jpeg', 'png'];
    const extension = file.name.split('.').pop()?.toLowerCase();
    return extension ? allowedExtensions.includes(extension) : false;
  },

  /**
   * 파일 업로드 통합 함수
   * 1. 파일 확장자 검증
   * 2. Presigned URL 요청
   * 3. S3 업로드
   * 4. key 반환
   *
   * @param folder 업로드할 폴더
   * @param file 업로드할 파일
   * @returns 업로드된 파일의 key
   */
  uploadFile: async (folder: FolderType, file: File): Promise<string> => {
    console.log('[FileService] Starting file upload process:', { folder, fileName: file.name, fileSize: file.size });
    
    // 파일 확장자 검증
    if (!fileService.isValidFileExtension(file)) {
      console.error('[FileService] Invalid file extension');
      throw new Error('허용되지 않은 파일 형식입니다. (jpg, jpeg, png만 가능)');
    }

    try {
      console.log('[FileService] Requesting presigned URL');
      // Presigned URL 요청
      const { uploadUrl, key } = await fileService.getPresignedUrl(folder, file);
      console.log('[FileService] Received presigned URL:', { key });

      // S3 업로드
      console.log('[FileService] Starting S3 upload');
      await fileService.uploadToS3(uploadUrl, file);
      console.log('[FileService] S3 upload completed successfully');

      return key;
    } catch (error) {
      console.error('[FileService] Upload failed:', error);
      if (error instanceof Error) {
        throw error;
      }
      throw new Error('파일 업로드에 실패했습니다.');
    }
  },
};

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
   * key 값을 사용하여 서버에서 이미지 다운로드 URL을 요청합니다.
   * 서버에서 URL을 받아올 수 없는 경우 fallback으로 S3 직접 URL을 구성하거나
   * 대체 이미지 URL을 반환합니다.
   * 
   * @param key 이미지 키 (예: 'classrooms/image-uuid.jpg')
   * @returns 이미지 다운로드 URL
   */
  getImageUrl: async (key: string | null): Promise<string> => {
    if (!key) return '';
    
    try {
      console.log('[FileService] 이미지 다운로드 URL 요청:', key);
      
      // 백엔드 요구사항에 맞게 key 파라미터만 전달
      const response = await axiosInstance.get(
        `/files/download-url?key=${encodeURIComponent(key)}`
      );

      console.log('[FileService] 다운로드 URL 응답:', response);
      
      // API 응답이 { uploadUrl, key } 형태인 경우
      if (response.data && response.data.uploadUrl) {
        console.log('[FileService] uploadUrl 사용:', response.data.uploadUrl);
        return response.data.uploadUrl;
      }
      
      // 이전 형식에 대한 fallback 처리
      if (typeof response.data === 'string') {
        return response.data;
      }
      
      if (response.data && response.data.downloadUrl) {
        return response.data.downloadUrl;
      }
      
      if (response.data && response.data.url) {
        return response.data.url;
      }
      
      if (response.data && response.data.isSuccess && response.data.data) {
        if (typeof response.data.data === 'string') {
          return response.data.data;
        }
        
        if (response.data.data.url) {
          return response.data.data.url;
        }
        
        if (response.data.data.downloadUrl) {
          return response.data.data.downloadUrl;
        }
        
        if (response.data.data.uploadUrl) {
          return response.data.data.uploadUrl;
        }
      }
      
      // 서버 응답에서 URL을 찾을 수 없는 경우 S3 직접 URL 구성
      console.log('[FileService] 서버 응답에서 URL을 찾을 수 없어 직접 S3 URL 구성');
      return `https://lufin-bucket.s3.ap-northeast-2.amazonaws.com/${key}`;
    } catch (error) {
      console.error('[FileService] 이미지 URL 가져오기 실패:', error);
      if (error instanceof Error) {
        console.error('[FileService] 오류 상세 정보:', error.message);
      }
      
      // 오류 발생 시 대체 이미지 URL 반환
      return `https://picsum.photos/400/200?random=${Math.floor(Math.random() * 1000)}`;
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

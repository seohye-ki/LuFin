import axiosInstance from '../axios';

/**
 * 파일 업로드 대상 폴더 타입
 * - classrooms: 교실 관련 이미지 (프로필, 배경 등)
 * - missions: 미션 관련 이미지 (미션 결과물, 첨부파일 등)
 */
export type FolderType = 'classrooms' | 'missions';

/**
 * Presigned URL 응답 인터페이스
 * @property uploadUrl - S3에 파일을 업로드할 수 있는 임시 URL
 * @property key - 업로드된 파일의 고유 식별자 (S3 경로)
 */
interface PresignedUrlResponse {
  uploadUrl: string;
  key: string;
}

/**
 * API 응답 공통 인터페이스
 * @template T - 응답 데이터의 타입
 */
interface ApiResponse<T> {
  isSuccess: boolean;
  data?: T;
  code?: string;
  message?: string;
}

/**
 * 파일 업로드 및 다운로드 관련 유틸리티 서비스
 *
 * 주요 기능:
 * 1. 이미지 업로드를 위한 Presigned URL 발급
 * 2. S3에 파일 직접 업로드
 * 3. 이미지 다운로드 URL 조회
 * 4. 파일 확장자 검증
 *
 * @example
 * // 이미지 업로드
 * const key = await fileService.uploadFile('classrooms', imageFile);
 *
 * // 이미지 URL 조회
 * const url = await fileService.getImageUrl(key);
 */
export const fileService = {
  /**
   * Presigned URL 요청
   *
   * @param folder - 업로드할 폴더 위치 ('classrooms' | 'missions')
   * @param file - 업로드할 파일 객체
   * @returns Presigned URL 정보 (uploadUrl, key)
   * @throws Error - URL 발급 실패 시
   *
   * @example
   * const { uploadUrl, key } = await fileService.getPresignedUrl('classrooms', file);
   */
  getPresignedUrl: async (folder: FolderType, file: File): Promise<PresignedUrlResponse> => {
    const response = await axiosInstance.get<
      ApiResponse<PresignedUrlResponse> | PresignedUrlResponse
    >(`/files/presigned-url?folder=${folder}&filename=${encodeURIComponent(file.name)}`);

    if ('uploadUrl' in response.data && 'key' in response.data) {
      return response.data as PresignedUrlResponse;
    }

    if (!response.data.isSuccess || !response.data.data) {
      throw new Error('파일 업로드 URL을 가져오는데 실패했습니다.');
    }

    return response.data.data;
  },

  /**
   * 이미지 다운로드 URL 조회
   *
   * @param key - 이미지의 고유 식별자 (S3 경로)
   * @returns 이미지 다운로드 URL
   * - 성공 시: Presigned URL 반환
   * - 실패 시: S3 직접 URL로 폴백
   * - key가 null인 경우: 빈 문자열 반환
   *
   * @example
   * const imageUrl = await fileService.getImageUrl('classrooms/image-123.jpg');
   * // <img src={imageUrl} alt="이미지" />
   */
  getImageUrl: async (key: string | null): Promise<string> => {
    if (!key) return '';

    try {
      const response = await axiosInstance.get(
        `/files/download-url?key=${encodeURIComponent(key)}`,
      );
      return response.data.uploadUrl;
    } catch {
      return `https://lufin-bucket.s3.ap-northeast-2.amazonaws.com/${key}`;
    }
  },

  /**
   * S3에 파일 직접 업로드
   *
   * @param uploadUrl - Presigned URL
   * @param file - 업로드할 파일
   * @throws Error - 업로드 실패 시
   *
   * @example
   * await fileService.uploadToS3(presignedUrl, file);
   */
  uploadToS3: async (uploadUrl: string, file: File): Promise<void> => {
    const response = await fetch(uploadUrl, {
      method: 'PUT',
      body: file,
      headers: {
        'Content-Type': file.type,
      },
      mode: 'cors',
      credentials: 'omit',
    });

    if (!response.ok) {
      throw new Error(`S3 upload failed: ${response.statusText}`);
    }
  },

  /**
   * 파일 확장자 검증
   * 허용된 확장자: jpg, jpeg, png
   *
   * @param file - 검증할 파일
   * @returns 유효한 확장자인지 여부
   *
   * @example
   * if (!fileService.isValidFileExtension(file)) {
   *   alert('지원하지 않는 파일 형식입니다.');
   *   return;
   * }
   */
  isValidFileExtension: (file: File): boolean => {
    const allowedExtensions = ['jpg', 'jpeg', 'png'];
    const extension = file.name.split('.').pop()?.toLowerCase();
    return extension ? allowedExtensions.includes(extension) : false;
  },

  /**
   * 파일 업로드 통합 프로세스
   * 1. 파일 확장자 검증
   * 2. Presigned URL 발급
   * 3. S3 업로드
   *
   * @param folder - 업로드할 폴더 ('classrooms' | 'missions')
   * @param file - 업로드할 파일
   * @returns 업로드된 파일의 key (S3 경로)
   * @throws Error - 파일 형식이 잘못되었거나 업로드 실패 시
   *
   * @example
   * try {
   *   const key = await fileService.uploadFile('classrooms', imageFile);
   *   // key를 서버에 저장하거나 필요한 곳에 사용
   * } catch (error) {
   *   console.error('이미지 업로드 실패:', error);
   * }
   */
  uploadFile: async (folder: FolderType, file: File): Promise<string> => {
    if (!fileService.isValidFileExtension(file)) {
      throw new Error('허용되지 않은 파일 형식입니다. (jpg, jpeg, png만 가능)');
    }

    const { uploadUrl, key } = await fileService.getPresignedUrl(folder, file);
    await fileService.uploadToS3(uploadUrl, file);
    return key;
  },
};

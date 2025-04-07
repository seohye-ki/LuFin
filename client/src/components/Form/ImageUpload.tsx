import { forwardRef, useState, useCallback } from 'react';
import { Icon } from '../Icon/Icon';

interface ImageUploadProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'value' | 'onChange'> {
  label?: string;
  description?: string;
  error?: string;
  isDisabled?: boolean;
  isLoading?: boolean;
  value?: File[];
  onChange?: (files: File[]) => void;
  maxFiles?: number;
  maxSize?: number; // in MB
  accept?: string;
}

const ImageUpload = forwardRef<HTMLInputElement, ImageUploadProps>(
  (
    {
      label,
      description,
      error,
      isDisabled,
      isLoading = false,
      value = [],
      onChange,
      maxFiles = 1,
      maxSize = 10, // 10MB default
      accept = 'image/png,image/jpeg,image/gif',
      className = '',
      ...props
    },
    ref,
  ) => {
    const [isDragging, setIsDragging] = useState(false);
    const [previews, setPreviews] = useState<string[]>([]);

    // 파일 처리 함수
    const handleFiles = useCallback(
      (files: FileList | null) => {
        if (!files || isDisabled || isLoading) return;

        const newFiles: File[] = [];
        const newPreviews: string[] = [];

        Array.from(files).forEach((file) => {
          // 파일 타입 체크
          if (!file.type.startsWith('image/')) return;

          // 파일 크기 체크 (MB)
          if (file.size > maxSize * 1024 * 1024) return;

          newFiles.push(file);
          newPreviews.push(URL.createObjectURL(file));
        });

        // 최대 파일 개수 제한
        const finalFiles = [...value, ...newFiles].slice(0, maxFiles);
        const finalPreviews = [...previews, ...newPreviews].slice(0, maxFiles);

        setPreviews(finalPreviews);
        onChange?.(finalFiles);
      },
      [isDisabled, isLoading, maxFiles, maxSize, onChange, previews, value],
    );

    // 드래그 앤 드롭 핸들러
    const handleDragOver = useCallback(
      (e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        if (!isDisabled && !isLoading) setIsDragging(true);
      },
      [isDisabled, isLoading],
    );

    const handleDragLeave = useCallback((e: React.DragEvent) => {
      e.preventDefault();
      e.stopPropagation();
      setIsDragging(false);
    }, []);

    const handleDrop = useCallback(
      (e: React.DragEvent) => {
        e.preventDefault();
        e.stopPropagation();
        setIsDragging(false);
        if (!isLoading) handleFiles(e.dataTransfer.files);
      },
      [handleFiles, isLoading],
    );

    // 파일 삭제 핸들러
    const handleRemove = useCallback(
      (index: number) => {
        if (isLoading) return;
        const newFiles = value.filter((_, i) => i !== index);
        const newPreviews = previews.filter((_, i) => i !== index);

        setPreviews(newPreviews);
        onChange?.(newFiles);
      },
      [isLoading, onChange, previews, value],
    );

    return (
      <div className={className}>
        {label && (
          <label htmlFor={props.id} className='block text-p2 font-medium text-dark-grey'>
            {label}
          </label>
        )}
        <div
          className={`mt-2 flex flex-col justify-center rounded-lg border-2 border-dashed
            ${isDragging ? 'border-info bg-info/5' : 'border-grey-30'}
            ${error ? 'border-danger bg-danger/5' : ''} 
            ${isDisabled || isLoading ? 'border-grey-30 bg-broken-white cursor-not-allowed' : ''}
          `}
          onDragOver={handleDragOver}
          onDragLeave={handleDragLeave}
          onDrop={handleDrop}
        >
          {previews.length > 0 ? (
            <div className='p-4'>
              <div className='flex flex-wrap gap-4'>
                {previews.map((preview, index) => (
                  <div key={preview} className='relative group'>
                    <div className='w-[200px] h-[200px] rounded-lg overflow-hidden shadow-sm'>
                      <img
                        src={preview}
                        alt={`Preview ${index + 1}`}
                        className='w-full h-full object-cover'
                      />
                      {!isDisabled && !isLoading && (
                        <div className='absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-all duration-200 flex items-center justify-center'>
                          <button
                            type='button'
                            onClick={() => handleRemove(index)}
                            className='p-2 rounded-full hover:bg-white/20 transition-colors'
                          >
                            <Icon name='Trash' size={24} color='white' />
                          </button>
                        </div>
                      )}
                    </div>
                    <div className='absolute bottom-2 left-2 px-2 py-1 bg-black/50 rounded text-p2 text-white'>
                      이미지 {index + 1}
                    </div>
                  </div>
                ))}
                {previews.length < maxFiles && (
                  <label
                    htmlFor={props.id}
                    className={`w-[200px] h-[200px] flex flex-col items-center justify-center rounded-lg border-2 border-dashed border-grey-30 
                      ${isDisabled || isLoading ? 'cursor-not-allowed' : 'cursor-pointer hover:border-info hover:bg-info/5'} transition-colors`}
                  >
                    {isLoading ? (
                      <>
                        <span className='text-p1 text-grey'>업로드 중...</span>
                      </>
                    ) : (
                      <>
                        <Icon name='GalleryAdd' size={32} color='grey-25' className='mb-2' />
                        <span className='text-p1 text-grey'>추가하기</span>
                      </>
                    )}
                    <input
                      ref={ref}
                      id={props.id}
                      type='file'
                      multiple={maxFiles > 1}
                      accept={accept}
                      disabled={isDisabled || isLoading}
                      className='sr-only'
                      onChange={(e) => handleFiles(e.target.files)}
                      {...props}
                    />
                  </label>
                )}
              </div>
            </div>
          ) : (
            <div className='px-6 py-10 text-center'>
              {isLoading ? (
                <>
                  <div className='mt-4 text-p1 text-grey'>업로드 중...</div>
                </>
              ) : (
                <>
                  <Icon
                    name='GalleryAdd'
                    size={48}
                    color={isDisabled ? 'grey-30' : 'grey-25'}
                    className='mx-auto'
                  />
                  <div className='mt-4 flex justify-center text-p1'>
                    <label
                      htmlFor={props.id}
                      className={`relative cursor-pointer rounded-md font-semibold
                        ${error ? 'text-danger' : isDisabled ? 'text-dark-grey cursor-not-allowed' : 'text-info hover:text-info/50'}`}
                    >
                      <span>파일 선택</span>
                      <input
                        ref={ref}
                        id={props.id}
                        type='file'
                        multiple={maxFiles > 1}
                        accept={accept}
                        disabled={isDisabled || isLoading}
                        className='sr-only'
                        onChange={(e) => handleFiles(e.target.files)}
                        {...props}
                      />
                    </label>
                  </div>
                </>
              )}
            </div>
          )}
        </div>
        {description && <p className='mt-2 text-p2 text-grey'>{description}</p>}
        {error && <p className='mt-2 text-p2 text-danger'>{error}</p>}
      </div>
    );
  },
);

ImageUpload.displayName = 'ImageUpload';

export default ImageUpload;

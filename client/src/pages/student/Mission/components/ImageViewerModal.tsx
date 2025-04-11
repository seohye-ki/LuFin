import { Icon } from '../../../../components/Icon/Icon';

interface ImageViewerModalProps {
  imageUrl: string;
  onClose: () => void;
}

const ImageViewerModal = ({ imageUrl, onClose }: ImageViewerModalProps) => {
  if (!imageUrl) return null;

  return (
    <>
      <div className='fixed inset-0 bg-black/60 z-50' onClick={onClose} />
      <div className='fixed inset-0 z-50 flex items-center justify-center'>
        <div className='relative max-w-[90vw] max-h-[90vh]'>
          <img
            src={imageUrl}
            alt='확대 이미지'
            className='w-full h-full object-contain rounded-xl shadow-xl'
          />
          <button
            onClick={onClose}
            className='absolute top-2 right-2 bg-white/80 backdrop-blur px-2 py-1 rounded-full'
          >
            <Icon name='Close' size={24} />
          </button>
        </div>
      </div>
    </>
  );
};

export default ImageViewerModal;

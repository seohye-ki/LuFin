import { useState } from 'react';
import Card from '../../../../components/Card/Card';
import Button from '../../../../components/Button/Button';
import TextField from '../../../../components/Form/TextField';
import ImageUpload from '../../../../components/Form/ImageUpload';
import { fileService } from '../../../../libs/services/file/fileService';
import useAlertStore from '../../../../libs/store/alertStore';
import { Classroom } from '../../../../libs/services/classroom/classroomService';

interface EditClassroomModalProps {
  classroom: Classroom;
  onClose: () => void;
  onSubmit: (data: {
    classId: number;
    title: string;
    school: string;
    grade: number;
    class: number;
    image?: File;
    key?: string | null;
  }) => void;
  isLoading?: boolean;
}

const EditClassroomModal = ({
  classroom,
  onClose,
  onSubmit,
  isLoading = false,
}: EditClassroomModalProps) => {
  const [formData, setFormData] = useState({
    title: classroom.name,
    school: classroom.school,
    grade: classroom.grade.toString(),
    class: classroom.classGroup.toString(),
    image: undefined as File | undefined,
    key: classroom.key,
  });
  const [isUploading, setIsUploading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.title || !formData.school || !formData.grade || !formData.class) {
      return;
    }

    onSubmit({
      classId: classroom.classId,
      ...formData,
      grade: Number(formData.grade),
      class: Number(formData.class),
    });
  };

  const handleImageUpload = async (files: File[]) => {
    const file = files[0];
    if (!file) return;

    try {
      console.log('[EditClassroomModal] Starting image upload:', {
        fileName: file.name,
        fileSize: file.size,
      });
      setIsUploading(true);
      const key = await fileService.uploadFile('classrooms', file);
      ('');
      setFormData((prev) => ({ ...prev, image: file, key }));
    } catch (error) {
      console.error('[EditClassroomModal] Image upload failed:', error);
      useAlertStore
        .getState()
        .showAlert(
          '이미지 업로드 실패',
          null,
          error instanceof Error ? error.message : '이미지 업로드에 실패했습니다.',
          'danger',
          {
            label: '확인',
            onClick: () => useAlertStore.getState().hideAlert(),
            color: 'primary',
          },
        );
    } finally {
      setIsUploading(false);
    }
  };

  return (
    <div className='fixed inset-0 bg-black/50 flex items-center justify-center z-50'>
      <Card className='w-[480px] max-h-[90vh] overflow-y-auto'>
        <form onSubmit={handleSubmit} className='flex flex-col gap-6'>
          <h2 className='text-h2 font-semibold'>클래스룸 수정하기</h2>

          <TextField
            label='클래스명'
            value={formData.title}
            onChange={(e) => setFormData({ ...formData, title: e.target.value })}
            placeholder='꿈나무 열정 교실'
            required
            inputSize='lg'
          />

          <TextField
            label='학교'
            value={formData.school}
            onChange={(e) => setFormData({ ...formData, school: e.target.value })}
            placeholder='문이차초등학교'
            required
            inputSize='lg'
          />

          <div className='flex gap-4'>
            <TextField
              label='학년'
              type='number'
              value={formData.grade}
              onChange={(e) => setFormData({ ...formData, grade: e.target.value })}
              placeholder='5'
              required
              className='flex-1'
              inputSize='lg'
            />
            <TextField
              label='반'
              type='number'
              value={formData.class}
              onChange={(e) => setFormData({ ...formData, class: e.target.value })}
              placeholder='2'
              required
              className='flex-1'
              inputSize='lg'
            />
          </div>

          <div className='[&>div>label]:text-h3'>
            <ImageUpload
              label='클래스 사진'
              onChange={handleImageUpload}
              accept='image/jpeg,image/jpg,image/png'
              isLoading={isUploading}
            />
          </div>

          <div className='flex gap-3 mt-4'>
            <Button
              type='button'
              color='neutral'
              full
              onClick={onClose}
              disabled={isLoading || isUploading}
            >
              취소
            </Button>
            <Button type='submit' color='info' full disabled={isLoading || isUploading}>
              {isLoading ? '수정 중...' : '수정하기'}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
};

export default EditClassroomModal;

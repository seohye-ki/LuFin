import { useState } from 'react';
import Card from '../../../../components/Card/Card';
import Button from '../../../../components/Button/Button';
import TextField from '../../../../components/Form/TextField';
import ImageUpload from '../../../../components/Form/ImageUpload';
import { fileService } from '../../../../libs/services/file/fileService';
import useAlertStore from '../../../../libs/store/alertStore';

interface CreateClassroomModalProps {
  onClose: () => void;
  onSubmit: (data: {
    title: string;
    school: string;
    grade: number;
    class: number;
    image?: File;
    key?: string | null;
  }) => void;
  isLoading?: boolean;
}

const CreateClassroomModal = ({
  onClose,
  onSubmit,
  isLoading = false,
}: CreateClassroomModalProps) => {
  const [formData, setFormData] = useState({
    title: '',
    school: '',
    grade: '',
    class: '',
    image: undefined as File | undefined,
    key: null as string | null,
  });

  const [isUploading, setIsUploading] = useState(false);
  const [titleError, setTitleError] = useState(false);
  const [schoolError, setSchoolError] = useState(false);
  const [gradeError, setGradeError] = useState(false);
  const [classError, setClassError] = useState(false);

  const validateTitle = (value: string) => value.trim() === '';
  const validateSchool = (value: string) => value.trim() === '';
  const validateGrade = (value: string) => {
    const number = Number(value);
    return isNaN(number) || number < 4 || number > 6;
  };
  const validateClass = (value: string) => {
    const number = Number(value);
    return isNaN(number) || number < 1 || number > 20;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const isTitleInvalid = validateTitle(formData.title);
    const isSchoolInvalid = validateSchool(formData.school);
    const isGradeInvalid = validateGrade(formData.grade);
    const isClassInvalid = validateClass(formData.class);

    setTitleError(isTitleInvalid);
    setSchoolError(isSchoolInvalid);
    setGradeError(isGradeInvalid);
    setClassError(isClassInvalid);

    if (isTitleInvalid || isSchoolInvalid || isGradeInvalid || isClassInvalid) return;

    onSubmit({
      ...formData,
      grade: Number(formData.grade),
      class: Number(formData.class),
    });
  };

  const handleImageUpload = async (files: File[]) => {
    const file = files[0];
    if (!file) return;

    try {
      setIsUploading(true);
      const key = await fileService.uploadFile('classrooms', file);
      setFormData((prev) => ({ ...prev, image: file, key }));
    } catch (error) {
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
      <Card className='w-100 max-h-[90vh] overflow-y-auto' titleLeft='새로운 클래스 생성'>
        <form onSubmit={handleSubmit} className='flex flex-col gap-6'>
          <TextField
            label='클래스명'
            value={formData.title}
            onChange={(e) => {
              const value = e.target.value;
              setFormData((prev) => ({ ...prev, title: value }));
              setTitleError(validateTitle(value));
            }}
            placeholder='꿈나무 열정 교실'
            inputSize='md'
            variant={titleError ? 'error' : 'normal'}
            description={titleError ? '클래스명을 입력해주세요.' : ''}
          />

          <TextField
            label='학교'
            value={formData.school}
            onChange={(e) => {
              const value = e.target.value;
              setFormData((prev) => ({ ...prev, school: value }));
              setSchoolError(validateSchool(value));
            }}
            placeholder='문이차초등학교'
            inputSize='md'
            variant={schoolError ? 'error' : 'normal'}
            description={schoolError ? '학교명을 입력해주세요.' : ''}
          />

          <div className='flex gap-4'>
            <TextField
              label='학년'
              type='number'
              value={formData.grade}
              onChange={(e) => {
                const value = e.target.value;
                setFormData((prev) => ({ ...prev, grade: value }));
                setGradeError(validateGrade(value));
              }}
              placeholder='5'
              className='flex-1'
              inputSize='md'
              variant={gradeError ? 'error' : 'normal'}
              description={gradeError ? '4~6학년만 선택할 수 있어요.' : ''}
            />

            <TextField
              label='반'
              type='number'
              value={formData.class}
              onChange={(e) => {
                const value = e.target.value;
                setFormData((prev) => ({ ...prev, class: value }));
                setClassError(validateClass(value));
              }}
              placeholder='2'
              className='flex-1'
              inputSize='md'
              variant={classError ? 'error' : 'normal'}
              description={classError ? '1~20반만 선택할 수 있어요.' : ''}
            />
          </div>

          <div className='[&>div>label]:text-c1'>
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
              size='md'
              onClick={onClose}
              disabled={isLoading || isUploading}
            >
              취소
            </Button>
            <Button size='md' type='submit' color='info' full disabled={isLoading || isUploading}>
              {isLoading ? '생성 중...' : '생성하기'}
            </Button>
          </div>
        </form>
      </Card>
    </div>
  );
};

export default CreateClassroomModal;

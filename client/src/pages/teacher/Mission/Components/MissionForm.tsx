import { useState, useRef } from 'react';
import TextField from '../../../../components/Form/TextField';
import Dropdown from '../../../../components/Form/Dropdown';
import TextArea from '../../../../components/Form/TextArea';
import ImageUpload from '../../../../components/Form/ImageUpload';
import Button from '../../../../components/Button/Button';
import { Icon } from '../../../../components/Icon/Icon';
import { MissionDetail } from '../../../../types/mission/mission';
import Card from '../../../../components/Card/Card';

interface MissionFormProps {
  defaultValues?: Partial<MissionDetail>;
  onClose: () => void;
  mode: 'create' | 'edit';
}

const MissionForm = ({ defaultValues = {}, onClose, mode }: MissionFormProps) => {
  const [title, setTitle] = useState(defaultValues?.title || '');
  const [difficulty, setDifficulty] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>(
    defaultValues.difficulty
      ? { type: 'star', count: defaultValues.difficulty as 1 | 2 | 3 }
      : null,
  );
  const [maxParticipant, setMaxParticipant] = useState(defaultValues?.maxParticipant || '');
  const [wage, setWage] = useState(defaultValues?.wage || '');
  const [content, setContent] = useState(defaultValues?.content || '');
  const [multipleImages, setMultipleImages] = useState<File[]>([]);

  const starDropdownItems = [
    { label: '', value: { type: 'star' as const, count: 1 as const } },
    { label: '', value: { type: 'star' as const, count: 2 as const } },
    { label: '', value: { type: 'star' as const, count: 3 as const } },
  ];

  const dropdownItems = [
    { label: '1', value: '1' },
    { label: '2', value: '2' },
    { label: '3', value: '3' },
  ];

  const isValidTitle = title.length >= 2 && title.length <= 30;
  const isValidDifficulty = difficulty !== null;
  const isValidMaxParticipant = maxParticipant !== '';
  const parsedWage = Number(wage);
  const isValidWage = !isNaN(parsedWage) && parsedWage >= 100 && parsedWage <= 10000000;
  const isValidContent = content.length <= 500;

  const titleRef = useRef<HTMLInputElement>(null);
  const wageRef = useRef<HTMLInputElement>(null);
  const contentRef = useRef<HTMLTextAreaElement>(null);

  const handleSubmit = () => {
    if (!isValidTitle) {
      titleRef.current?.focus();
      return;
    }
    if (!isValidDifficulty) {
      document.getElementById('difficulty')?.focus();
      return;
    }
    if (!isValidMaxParticipant) {
      document.getElementById('maxParticipant')?.focus();
      return;
    }
    if (!isValidWage) {
      wageRef.current?.focus();
      return;
    }
    if (!isValidContent) {
      contentRef.current?.focus();
      return;
    }
  };

  return (
    <Card
      titleLeft={mode === 'create' ? '미션 생성하기' : '미션 수정하기'}
      titleRight={
        <div onClick={onClose}>
          <Icon name='Close' size={24} color='#8A8D8E' />
        </div>
      }
      titleSize='l'
      isModal
    >
      <div className='flex flex-col gap-2 overflow-y-auto sm:max-h-[80vh] md:max-h-[60vh] [&::-webkit-scrollbar]:hidden'>
        <TextField
          id='title'
          name='title'
          label='제목'
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          variant={!isValidTitle && title ? 'error' : 'normal'}
          description={!isValidTitle && title ? '제목은 2자 이상, 30자 이하로 입력해주세요.' : ''}
          ref={titleRef}
        />
        <Dropdown
          label='난이도'
          items={starDropdownItems}
          value={difficulty}
          onChange={(value) => setDifficulty(value as { type: 'star'; count: 1 | 2 | 3 })}
        />
        <Dropdown
          label='참여 인원'
          items={dropdownItems}
          value={maxParticipant}
          onChange={(value) => setMaxParticipant(value as string)}
        />
        <TextField
          id='wage'
          name='wage'
          label='보상'
          value={wage}
          onChange={(e) => setWage(e.target.value)}
          variant={!isValidWage && wage ? 'error' : 'normal'}
          description={
            !isValidWage && wage ? '100루핀 이상, 10000000루핀 이하로 입력해주세요.' : ''
          }
          ref={wageRef}
        />
        <TextArea
          id='content'
          name='content'
          label='미션 설명'
          placeholder='최대 500자까지 입력가능합니다.'
          rows={4}
          value={content}
          onChange={(e) => setContent(e.target.value)}
          variant={!isValidContent && content ? 'error' : 'normal'}
          description={!isValidContent && content ? '최대 500자까지 입력가능합니다.' : ''}
          ref={contentRef}
        />
        <ImageUpload
          id='images'
          label='미션 참고 이미지'
          value={multipleImages}
          onChange={(value) => setMultipleImages(value as File[])}
        />
        <div className='flex gap-2'>
          <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
            취소
          </Button>
          <Button variant='solid' color='primary' size='md' full onClick={handleSubmit}>
            생성하기
          </Button>
        </div>
      </div>
    </Card>
  );
};

export default MissionForm;

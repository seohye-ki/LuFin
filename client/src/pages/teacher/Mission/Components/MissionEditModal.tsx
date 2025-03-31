import { MissionDetail } from '../../../../types/Mission/mission';
import Card from '../../../../components/Card/Card';
import { Icon } from '../../../../components/Icon/Icon';
import TextField from '../../../../components/Form/TextField';
import Dropdown from '../../../../components/Form/Dropdown';
import TextArea from '../../../../components/Form/TextArea';
import ImageUpload from '../../../../components/Form/ImageUpload';
import Button from '../../../../components/Button/Button';
import { useState } from 'react';

interface MissionEditModalProps {
  mission: MissionDetail;
  onClose: () => void;
}

const MissionEditModal = ({ mission, onClose }: MissionEditModalProps) => {
  const [title, setTitle] = useState(mission.title);
  const [difficulty, setDifficulty] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>({
    type: 'star',
    count: mission.difficulty as 1 | 2 | 3,
  });
  const [maxParticipant, setMaxParticipant] = useState(mission.maxParticipant.toString());
  const [wage, setWage] = useState(mission.wage.toString());
  const [content, setContent] = useState(mission.content);
  const [images, setImages] = useState<File[]>([]);

  const starDropdownItems = [
    { label: '', value: { type: 'star' as const, count: 1 as const } },
    { label: '', value: { type: 'star' as const, count: 2 as const } },
    { label: '', value: { type: 'star' as const, count: 3 as const } },
  ];

  const participantDropdownItems = [
    { label: '1명', value: '1' },
    { label: '2명', value: '2' },
    { label: '3명', value: '3' },
  ];

  return (
    <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl bg-white shadow-lg z-50'>
      <Card
        titleLeft='미션 수정하기'
        titleRight={
          <div onClick={onClose}>
            <Icon name='Close' size={24} color='#8A8D8E' />
          </div>
        }
        titleSize='l'
        isModal
      >
        <div className='flex flex-col gap-4 overflow-y-auto sm:max-h-[80vh] md:max-h-[60vh] [&::-webkit-scrollbar]:hidden'>
          <TextField
            id='title'
            name='title'
            type='text'
            label='미션'
            value={title}
            onChange={(e) => setTitle(e.target.value)}
          />
          <Dropdown
            label='난이도'
            items={starDropdownItems}
            value={difficulty}
            onChange={(value) => setDifficulty(value as typeof difficulty)}
          />
          <Dropdown
            label='최대 참여 인원'
            items={participantDropdownItems}
            value={maxParticipant}
            onChange={(value) => setMaxParticipant(value as string)}
          />
          <TextField
            id='wage'
            name='wage'
            type='number'
            label='보상'
            value={wage}
            onChange={(e) => setWage(e.target.value)}
          />
          <TextArea
            id='content'
            name='content'
            label='설명'
            value={content}
            onChange={(e) => setContent(e.target.value)}
            rows={4}
          />
          <ImageUpload
            id='images'
            label='미션 이미지'
            description='최대 4장까지 업로드 가능합니다'
            maxFiles={4}
            value={images}
            onChange={setImages}
          />
          <div className='flex gap-2'>
            <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
              취소
            </Button>
            <Button variant='solid' color='primary' size='md' full>
              수정하기
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default MissionEditModal;

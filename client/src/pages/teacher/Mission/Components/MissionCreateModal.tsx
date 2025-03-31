import Card from '../../../../components/Card/Card';
import { Icon } from '../../../../components/Icon/Icon';
import TextField from '../../../../components/Form/TextField';
import ImageUpload from '../../../../components/Form/ImageUpload';
import Dropdown from '../../../../components/Form/Dropdown';
import TextArea from '../../../../components/Form/TextArea';
import { useState } from 'react';
import Button from '../../../../components/Button/Button';

interface MissionCreateModalProps {
  onClose: () => void;
}

const MissionCreateModal = ({ onClose }: MissionCreateModalProps) => {
  const [multipleImages, setMultipleImages] = useState<File[]>([]);
  const [selectedStars, setSelectedStars] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>(
    null,
  );
  const [selectedValue, setSelectedValue] = useState<
    string | number | { type: 'star'; count: 1 | 2 | 3 } | null
  >(null);
  const [textAreaValue, setTextAreaValue] = useState('');
  const [textAreaStatus, setTextAreaStatus] = useState<'normal' | 'success' | 'error'>('normal');
  const [textAreaMessage, setTextAreaMessage] = useState('');

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

  return (
    <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl border-t border-new-grey shadow-lg z-20'>
      <Card
        titleLeft='미션 생성하기'
        titleRight={
          <div onClick={onClose}>
            <Icon name='Close' size={24} color='#8A8D8E' />
          </div>
        }
        titleSize='l'
        isModal
      >
        <div
          className='flex flex-col gap-2 overflow-y-auto sm:max-h-[80vh] md:max-h-[60vh] [&::-webkit-scrollbar]:hidden'
          style={{
            scrollbarWidth: 'none', // Firefox
            msOverflowStyle: 'none', // IE/Edge
          }}
        >
          <TextField
            id='email'
            name='email'
            type='email'
            label='이메일'
            placeholder='you@example.com'
          />
          <Dropdown
            label='난이도'
            items={starDropdownItems}
            value={selectedStars}
            onChange={(value) => setSelectedStars(value as typeof selectedStars)}
          />
          <Dropdown
            label='참여 인원'
            items={dropdownItems}
            value={selectedValue}
            onChange={(value) => setSelectedValue(value as typeof selectedValue)}
          />
          <TextField id='wage' name='wage' type='number' label='보상' placeholder='1000' />
          <TextArea
            id='validation'
            name='validation'
            label='미션 설명'
            placeholder='최대 500자까지 입력가능합니다.'
            rows={4}
            value={textAreaValue}
            onChange={(e) => {
              const value = e.target.value;
              setTextAreaValue(value);
              if (value.length <= 500) {
                setTextAreaStatus('success');
              } else {
                setTextAreaStatus('error');
                setTextAreaMessage('최대 500자까지 입력가능합니다.');
              }
            }}
            variant={textAreaStatus}
            description={textAreaMessage}
          />
          <ImageUpload
            id='multiple-images'
            label='미션 인증 이미지'
            description='최대 4장까지 업로드 가능합니다'
            maxFiles={4}
            value={multipleImages}
            onChange={setMultipleImages}
          />
          <div className='flex gap-2'>
            <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
              취소
            </Button>
            <Button variant='solid' color='primary' size='md' full>
              생성하기
            </Button>
          </div>
        </div>
      </Card>
    </div>
  );
};

export default MissionCreateModal;

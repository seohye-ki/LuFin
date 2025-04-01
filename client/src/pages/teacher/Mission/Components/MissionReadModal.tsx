import { MissionDetail } from '../../../../types/Mission/mission';
import { members } from '../../../../types/Member/member';
import Card from '../../../../components/Card/Card';
import { Icon } from '../../../../components/Icon/Icon';
import Badge from '../../../../components/Badge/Badge';
import Lufin from '../../../../components/Lufin/Lufin';
import Button from '../../../../components/Button/Button';
import { useState } from 'react';
import Profile from '../../../../components/Profile/Profile';
import MissionEditModal from './MissionEditModal';
import Alert from '../../../../components/Alert/Alert';

interface MissionReadModalProps {
  mission: MissionDetail;
  onClose: () => void;
}

const MissionReadModal = ({ mission, onClose }: MissionReadModalProps) => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isDeleteMode, setIsDeleteMode] = useState(false);
  const [isApproveMode, setIsApproveMode] = useState(false);
  const [selectedParticipation, setSelectedParticipation] = useState<number | null>(null);

  const handlePrevImage = () => {
    setCurrentImageIndex((prev) => (prev === 0 ? mission.missionImages.length - 2 : prev - 2));
  };

  const handleNextImage = () => {
    setCurrentImageIndex((prev) => (prev >= mission.missionImages.length - 2 ? 0 : prev + 2));
  };

  const getVisibleImages = () => {
    const images = [];
    for (let i = 0; i < 2; i++) {
      const index = (currentImageIndex + i) % mission.missionImages.length;
      images.push(mission.missionImages[index]);
    }
    return images;
  };

  const getStatusBadge = (status: string) => {
    switch (status) {
      case 'SUCCESS':
        return <Badge status='done'>완료</Badge>;
      case 'IN_PROGRESS':
        return <Badge status='ing'>진행중</Badge>;
      case 'CHECKING':
        return <Badge status='ing'>검토 대기중</Badge>;
      case 'FAILED':
        return <Badge status='fail'>실패</Badge>;
      case 'REJECTED':
        return <Badge status='reject'>거절</Badge>;
      default:
        return null;
    }
  };

  const handleParticipationClick = (participationId: number, status: string) => {
    if (status === 'CHECKING') {
      setSelectedParticipation(participationId);
      setIsApproveMode(true);
    }
  };

  if (isEditMode) {
    return <MissionEditModal mission={mission} onClose={() => setIsEditMode(false)} />;
  }

  if (isDeleteMode) {
    return (
      <div className='fixed inset-0 flex items-center justify-center z-50'>
        <Alert
          title='미션을 삭제하시겠습니까?'
          description='삭제된 미션은 복구할 수 없습니다.'
          status='danger'
          primaryButton={{
            label: '취소',
            onClick: () => {
              // TODO: 미션 삭제 API 호출
              onClose();
            },
            color: 'neutral',
          }}
          secondaryButton={{
            label: '삭제하기',
            onClick: () => setIsDeleteMode(false),
            color: 'danger',
          }}
          buttonDirection='row'
        />
      </div>
    );
  }

  if (isApproveMode) {
    return (
      <div className='fixed inset-0 flex items-center justify-center z-50'>
        <Alert
          title='미션을 승인하시겠습니까?'
          description={
            <div className='flex flex-col gap-2'>
              <Profile
                name={
                  members.find((m) => m.memberId === selectedParticipation)?.name || '알 수 없음'
                }
                profileImage={
                  members.find((m) => m.memberId === selectedParticipation)?.profileImage ||
                  'https://picsum.photos/200/300?random=1'
                }
                variant='row'
              />
              <span className='text-c1'>승인하면 보상이 지급됩니다.</span>
            </div>
          }
          status='success'
          primaryButton={{
            label: '취소',
            onClick: () => {
              setIsApproveMode(false);
              setSelectedParticipation(null);
            },
            color: 'neutral',
          }}
          secondaryButton={{
            label: '승인하기',
            onClick: () => {
              // TODO: 미션 승인 API 호출
              setIsApproveMode(true);
              setSelectedParticipation(null);
            },
            color: 'primary',
          }}
          buttonDirection='row'
        />
      </div>
    );
  }

  return (
    <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl bg-white shadow-lg z-50'>
      <Card
        titleLeft='오늘의 미션'
        titleRight={
          <div className='flex gap-2'>
            <button onClick={() => setIsEditMode(true)}>
              <Icon name='CircleEdit' size={32} />
            </button>
            <button onClick={() => setIsDeleteMode(true)}>
              <Icon name='CircleTrash' size={32} />
            </button>
          </div>
        }
        titleSize='l'
        isModal
      >
        <div className='flex flex-col gap-2 overflow-y-auto sm:max-h-[80vh] md:max-h-[60vh] [&::-webkit-scrollbar]:hidden'>
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>미션</span>
            <span className='text-p1 font-semibold'>{mission.title}</span>
          </div>
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>난이도</span>
            <div className='flex items-center'>
              {Array(mission.difficulty)
                .fill(0)
                .map((_, i) => (
                  <Icon key={i} name='Star' />
                ))}
            </div>
          </div>
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>참여 인원</span>
            <div className='flex flex-col gap-2'>
              {mission.missionParticipations.map((participation) => {
                const member = members.find((m) => m.memberId === participation.memberId);
                return (
                  <div
                    key={participation.participationId}
                    className={`flex items-center gap-2 ${
                      participation.status === 'CHECKING'
                        ? 'cursor-pointer hover:bg-grey-50 p-2 rounded-lg'
                        : ''
                    }`}
                    onClick={() =>
                      handleParticipationClick(participation.participationId, participation.status)
                    }
                  >
                    <Profile
                      name={member?.name || '알 수 없음'}
                      variant='row'
                      profileImage={
                        member?.profileImage || 'https://picsum.photos/200/300?random=1'
                      }
                    />
                    {getStatusBadge(participation.status)}
                  </div>
                );
              })}
            </div>
          </div>
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>보상</span>
            <Lufin size='s' count={mission.wage} />
          </div>
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>설명</span>
            <span className='text-p1 font-semibold'>{mission.content}</span>
          </div>
          {mission.missionImages.length > 0 && (
            <div className='mt-2 relative'>
              <div className='grid grid-cols-2 gap-2'>
                {getVisibleImages().map((image) => (
                  <div key={image.missionImageId} className='relative aspect-square'>
                    <img
                      src={image.imageUrl}
                      alt='미션 인증 이미지'
                      className='w-full h-full object-cover rounded-lg'
                    />
                  </div>
                ))}
              </div>
              {mission.missionImages.length > 2 && (
                <>
                  <button
                    onClick={handlePrevImage}
                    className='absolute left-2 top-1/2 -translate-y-1/2 bg-light-cyan-30 bg-opacity-50 text-white p-2 rounded-full'
                  >
                    <Icon name='ArrowLeft2' size={20} />
                  </button>
                  <button
                    onClick={handleNextImage}
                    className='absolute right-2 top-1/2 -translate-y-1/2 bg-light-cyan-30 bg-opacity-50 text-white p-2 rounded-full'
                  >
                    <Icon name='ArrowRight2' size={20} />
                  </button>
                  <div className='absolute bottom-2 left-1/2 -translate-x-1/2 flex gap-1'>
                    {Array.from({ length: Math.ceil(mission.missionImages.length / 2) }).map(
                      (_, index) => (
                        <div
                          key={index}
                          className={`w-2 h-2 rounded-full ${
                            index === Math.floor(currentImageIndex / 2) ? 'bg-white' : 'bg-white/50'
                          }`}
                        />
                      ),
                    )}
                  </div>
                </>
              )}
            </div>
          )}
        </div>
        <div className='flex gap-2 mt-2'>
          <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
            취소
          </Button>
          <Button variant='solid' color='primary' size='md' full>
            확인
          </Button>
        </div>
      </Card>
    </div>
  );
};

export default MissionReadModal;

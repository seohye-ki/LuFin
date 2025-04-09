import Card from '../../../../components/Card/Card';
import Badge from '../../../../components/Badge/Badge';
import Button from '../../../../components/Button/Button';
import { MissionRaw } from '../../../../types/mission/mission';
import { Icon } from '../../../../components/Icon/Icon';
import Lufin from '../../../../components/Lufin/Lufin';
import { useState } from 'react';
import { getStatusBadge } from '../../../../libs/utils/mission-util';
import useMissionStore from '../../../../libs/store/missionStore';
import useAlertStore from '../../../../libs/store/alertStore';

interface MyMissionModalProps {
  onClose: () => void;
  mission: MissionRaw;
  isMyMission: boolean;
  participationId?: number;
  onSuccess?: () => void;
}

const MyMissionModal = ({
  onClose,
  mission,
  isMyMission,
  participationId,
  onSuccess,
}: MyMissionModalProps) => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const applyMission = useMissionStore((state) => state.applyMission);
  const requestReview = useMissionStore((state) => state.requestReview);

  const status = getStatusBadge();

  const handlePrevImage = () => {
    setCurrentImageIndex((prev) => (prev === 0 ? mission.images.length - 2 : prev - 2));
  };

  const handleNextImage = () => {
    setCurrentImageIndex((prev) => (prev >= mission.images.length - 2 ? 0 : prev + 2));
  };

  const getVisibleImages = () => {
    const images = [];
    for (let i = 0; i < 2; i++) {
      const index = (currentImageIndex + i) % mission.images.length;
      images.push(mission.images[index]);
    }
    return images;
  };

  const handlePrimaryAction = async () => {
    // ✅ 수행 가능 미션: 신청
    if (!isMyMission) {
      const result = await applyMission(mission.missionId);
      if (result.success) {
        useAlertStore
          .getState()
          .showAlert('미션 신청이 완료되었습니다.', null, result.message || '', 'success', {
            label: '확인',
            onClick: () => {
              useAlertStore.getState().hideAlert();
              onClose();
              onSuccess?.();
            },
            color: 'neutral',
          });
      } else {
        useAlertStore
          .getState()
          .showAlert(
            '미션 신청에 실패했습니다.',
            null,
            result.message || '신청에 실패했습니다.',
            'danger',
            {
              label: '확인',
              onClick: () => {
                useAlertStore.getState().hideAlert();
                onClose();
              },
              color: 'neutral',
            },
          );
      }
      return;
    }

    // ✅ 나의 미션: 리뷰 요청
    if (participationId === undefined) {
      useAlertStore
        .getState()
        .showAlert(
          '참여자 정보를 찾을 수 없습니다.',
          null,
          '참여자 정보를 찾을 수 없습니다.',
          'danger',
          {
            label: '확인',
            onClick: () => {
              useAlertStore.getState().hideAlert();
              onClose();
            },
          },
        );
      return;
    }

    const result = await requestReview(participationId);
    if (result.success) {
      useAlertStore
        .getState()
        .showAlert(
          '리뷰 요청이 완료되었습니다.',
          null,
          result.message || '리뷰 요청에 실패했습니다.',
          'success',
          {
            label: '확인',
            onClick: () => {
              useAlertStore.getState().hideAlert();
              onClose();
            },
            color: 'neutral',
          },
        );
    } else {
      useAlertStore
        .getState()
        .showAlert(
          '리뷰 요청에 실패했습니다.',
          null,
          result.message || '리뷰 요청에 실패했습니다.',
          'danger',
          {
            label: '확인',
            onClick: () => {
              useAlertStore.getState().hideAlert();
              onClose();
            },
            color: 'neutral',
          },
        );
    }
  };

  return (
    <Card
      titleLeft={isMyMission ? '나의 미션' : '수행 가능 미션'}
      titleRight={<Badge status={status.status}>{status.text}</Badge>}
      titleSize='l'
      isModal
    >
      <div className='flex flex-col w-[352px] gap-2 overflow-y-auto sm:max-h-[80vh] md:max-h-[60vh] [&::-webkit-scrollbar]:hidden'>
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
            {`${mission.currentParticipants}/${mission.maxParticipants}`}
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
        {mission.images.length > 0 && (
          <div className='flex flex-col gap-2'>
            <span className='text-c1 text-grey'>사진</span>
            <div className='mt-2 relative'>
              <div className='grid grid-cols-2 gap-2'>
                {getVisibleImages().map((image, index) => (
                  <div key={`${image}-${index}`} className='relative aspect-square'>
                    <img
                      src={image}
                      alt='미션 인증 이미지'
                      className='w-full h-full object-cover rounded-lg'
                    />
                  </div>
                ))}
              </div>
              {mission.images.length > 2 && (
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
                    {Array.from({ length: Math.ceil(mission.images.length / 2) }).map(
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
          </div>
        )}
      </div>
      <div className='flex gap-2 mt-2'>
        <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
          취소
        </Button>
        <Button variant='solid' color='primary' size='md' full onClick={handlePrimaryAction}>
          {isMyMission ? '리뷰 요청하기' : '신청하기'}
        </Button>
      </div>
    </Card>
  );
};

export default MyMissionModal;

import { MissionRaw, ParticipationUserInfo } from '../../../../types/mission/mission';
import Card from '../../../../components/Card/Card';
import { Icon } from '../../../../components/Icon/Icon';
import Badge from '../../../../components/Badge/Badge';
import Lufin from '../../../../components/Lufin/Lufin';
import Button from '../../../../components/Button/Button';
import { useState, useEffect } from 'react';
import Profile from '../../../../components/Profile/Profile';
import MissionEditModal from './MissionEditModal';
import useAlertStore, { hideGlobalAlert, showGlobalAlert } from '../../../../libs/store/alertStore';
import useMissionStore from '../../../../libs/store/missionStore';
import { fileService } from '../../../../libs/services/file/fileService';
import ImageViewerModal from '../../../../pages/student/Mission/components/ImageViewerModal';

interface MissionReadModalProps {
  mission: MissionRaw;
  onClose: () => void;
}

const MissionReadModal = ({ mission, onClose }: MissionReadModalProps) => {
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isEditMode, setIsEditMode] = useState(false);
  const [isDeleteMode, setIsDeleteMode] = useState(false);
  const [isApproveMode, setIsApproveMode] = useState(false);
  const [participations, setParticipations] = useState<ParticipationUserInfo[]>([]);
  const [selectedParticipation, setSelectedParticipation] = useState<ParticipationUserInfo | null>(
    null,
  );
  const [imageUrls, setImageUrls] = useState<string[]>([]);
  const [selectedImageUrl, setSelectedImageUrl] = useState<string | null>(null);

  useEffect(() => {
    const loadImages = async () => {
      try {
        if (mission.images && mission.images.length > 0) {
          const urls = await Promise.all(
            mission.images.map((img) => fileService.getImageUrl(img.objectKey)),
          );
          setImageUrls(urls);
        }
      } catch (error) {
        console.error('이미지 로드 오류:', error);
      }
    };

    loadImages();
  }, [mission.images]);

  const { deleteMission, getParticipationList, changeMissionStatus, getMissionList } =
    useMissionStore();

  useEffect(() => {
    const fetchParticipation = async () => {
      const result = await getParticipationList(mission.missionId);
      if (result.success) {
        setParticipations(result.participations ?? []);
        console.log(result.participations);
      } else {
        useAlertStore
          .getState()
          .showAlert(
            '참여자 목록 조회에 실패했습니다.',
            '',
            result.message || '알 수 없는 오류가 발생했습니다.',
            'danger',
            {
              label: '확인',
              onClick: () => {},
              color: 'neutral',
            },
          );
      }
    };
    fetchParticipation();
  }, [mission.missionId]);

  useEffect(() => {
    if (isDeleteMode) {
      useAlertStore.getState().showAlert(
        '미션을 삭제하시겠습니까?',
        null,
        '삭제된 미션은 복구할 수 없습니다.',
        'danger',
        {
          label: '취소',
          onClick: () => {
            useAlertStore.setState({ isVisible: false, isOpening: false });
          },
          color: 'neutral',
        },
        {
          label: '삭제하기',
          onClick: async () => {
            await deleteMission(mission.missionId);
            useAlertStore.setState({ isVisible: false, isOpening: false });
            onClose();
            await getMissionList();
            setIsDeleteMode(false);
          },
          color: 'danger',
        },
      );
    }
  }, [isDeleteMode]);

  useEffect(() => {
    if (isApproveMode && selectedParticipation) {
      useAlertStore.getState().showAlert(
        '미션을 승인하시겠습니까?',
        <div className='flex flex-col justify-center items-center gap-2'>
          <Profile
            name={selectedParticipation.name}
            profileImage={selectedParticipation.profileImage}
            variant='row'
          />
          <span className='text-c1'>승인하면 보상이 지급됩니다.</span>
        </div>,
        '',
        'success',
        {
          label: '취소',
          onClick: () => {
            setIsApproveMode(false);
            setSelectedParticipation(null);
          },
          color: 'neutral',
        },
        {
          label: '승인하기',
          onClick: async () => {
            await changeMissionStatus(
              mission.missionId,
              selectedParticipation.participationId,
              'SUCCESS',
            );
            setIsApproveMode(false);
            setSelectedParticipation(null);
            showGlobalAlert(
              '미션 승인이 완료 됐습니다.',
              null,
              '즉시 보상이 지급됩니다.',
              'success',
              {
                label: '확인',
                onClick: () => {
                  setIsApproveMode(false);
                  setSelectedParticipation(null);
                  hideGlobalAlert();
                },
              },
            );
          },
          color: 'primary',
        },
      );
    }
  }, [isApproveMode, selectedParticipation]);

  const handlePrevImage = () => {
    setCurrentImageIndex((prev) => (prev === 0 ? imageUrls.length - 2 : prev - 2));
  };

  const handleNextImage = () => {
    setCurrentImageIndex((prev) => (prev >= imageUrls.length - 2 ? 0 : prev + 2));
  };

  const getVisibleImages = () => {
    if (imageUrls.length === 1) return imageUrls;
    const images = [];
    for (let i = 0; i < 2; i++) {
      const index = (currentImageIndex + i) % imageUrls.length;
      images.push(imageUrls[index]);
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
        return <Badge status='review'>검토 대기중</Badge>;
      case 'FAILED':
        return <Badge status='fail'>실패</Badge>;
      case 'REJECTED':
        return <Badge status='reject'>거절</Badge>;
      default:
        return null;
    }
  };

  const handleParticipationClick = (p: ParticipationUserInfo) => {
    if (p.status === 'CHECKING') {
      setSelectedParticipation(p);
      setIsApproveMode(true);
    }
  };

  if (isEditMode) {
    return (
      <MissionEditModal
        mission={mission}
        onClose={() => setIsEditMode(false)}
        selectedDate={new Date(mission.missionDate)}
      />
    );
  }

  return (
    <>
      <div className='fixed inset-0 bg-black z-10' style={{ opacity: 0.5 }} onClick={onClose} />
      <div className='fixed top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[416px] max-h-screen rounded-xl bg-white shadow-lg z-40'>
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
              {participations.length > 0 ? (
                <div className='flex flex-col gap-2'>
                  {participations.map((p) => (
                    <div
                      key={p.name}
                      className={`flex items-center gap-2 ${p.status === 'CHECKING' ? 'cursor-pointer hover:bg-grey-50 p-2 rounded-lg' : ''}`}
                      onClick={() => handleParticipationClick(p)}
                    >
                      <span className='text-p1 font-semibold'>{p.name}</span>
                      {getStatusBadge(p.status)}
                    </div>
                  ))}
                </div>
              ) : (
                <span className='text-p1 text-grey font-semibold'>참여자가 없습니다.</span>
              )}
            </div>
            <div className='flex flex-col gap-2'>
              <span className='text-c1 text-grey'>보상</span>
              <Lufin size='s' count={mission.wage} />
            </div>
            <div className='flex flex-col gap-2'>
              <span className='text-c1 text-grey'>설명</span>
              <span className='text-p1 font-semibold'>{mission.content}</span>
            </div>
            {imageUrls.length > 0 && (
              <div className='mt-2 relative'>
                <div className='grid grid-cols-2 gap-2'>
                  {getVisibleImages().map((image) => (
                    <div key={image} className='relative aspect-square'>
                      <img
                        src={image}
                        alt='미션 인증 이미지'
                        onClick={() => setSelectedImageUrl(image)}
                        className='w-full h-full object-cover rounded-lg'
                      />
                    </div>
                  ))}
                </div>
                {imageUrls.length > 2 && (
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
                  </>
                )}
              </div>
            )}
          </div>
          <div className='flex gap-2 mt-2'>
            <Button variant='solid' color='neutral' size='md' full onClick={onClose}>
              취소
            </Button>
            <Button variant='solid' color='primary' size='md' full onClick={onClose}>
              확인
            </Button>
          </div>
        </Card>
      </div>
      {selectedImageUrl && (
        <ImageViewerModal imageUrl={selectedImageUrl} onClose={() => setSelectedImageUrl(null)} />
      )}
    </>
  );
};

export default MissionReadModal;

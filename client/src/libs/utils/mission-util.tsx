import { MissionList, MissionParticipation } from '../../types/mission/mission';
import { Icon } from '../../components/Icon/Icon';
import Badge from '../../components/Badge/Badge';
import Button from '../../components/Button/Button';
import Lufin from '../../components/Lufin/Lufin';

export const getStatusBadge = (participation?: MissionParticipation) => {
  if (!participation) {
    return { status: 'ready' as const, text: '모집 중' };
  }
  switch (participation.status) {
    case 'SUCCESS':
      return { status: 'done' as const, text: '성공' };
    case 'RECRUITING':
      return { status: 'ing' as const, text: '수행 중' };
    case 'CHECKING':
      return { status: 'review' as const, text: '검토 중' };
    case 'FAILED':
      return { status: 'fail' as const, text: '실패' };
    case 'REJECTED':
      return { status: 'reject' as const, text: '거절' };
    default:
      return { status: 'ready' as const, text: '모집 중' };
  }
};

export const createMissionRow = (
  mission: MissionList,
  participation: MissionParticipation | undefined,
  onClick: (mission: MissionList, participationId?: number) => void,
) => {
  const difficultyStars = (
    <div className='flex items-center'>
      {Array(mission.difficulty)
        .fill(0)
        .map((_, i) => (
          <Icon key={i} name='Star' />
        ))}
    </div>
  );

  const isJoined = Boolean(participation);
  const status = participation?.status;
  const date = new Date(mission.missionDate);
  const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1)
    .toString()
    .padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;

  const isDisabled =
    !!participation && ['SUCCESS', 'FAILED', 'REJECTED'].includes(participation.status);

  return {
    title: mission.title,
    wage: <Lufin size='s' count={mission.wage} />,
    difficulty: difficultyStars,
    participants: `${mission.currentParticipants}/${mission.maxParticipants}`,
    createdAt: formattedDate,
    participationId: participation?.participationId,
    status: (
      <Badge
        status={
          !status
            ? 'ready'
            : status === 'SUCCESS'
              ? 'done'
              : status === 'RECRUITING'
                ? 'ing'
                : status === 'CHECKING'
                  ? 'review'
                  : status === 'FAILED'
                    ? 'fail'
                    : 'reject'
        }
      >
        {!status
          ? '모집 중'
          : status === 'SUCCESS'
            ? '성공'
            : status === 'RECRUITING'
              ? '수행 중'
              : status === 'CHECKING'
                ? '검토 중'
                : status === 'FAILED'
                  ? '실패'
                  : '거절'}
      </Badge>
    ),
    action: (
      <div className='h-10 w-[128px] flex items-center justify-center'>
        {isJoined ? (
          status === 'RECRUITING' ? (
            <Button
              variant='ghost'
              color='info'
              size='md'
              onClick={() => onClick(mission, participation?.participationId)}
            >
              리뷰 요청하기
            </Button>
          ) : null
        ) : (
          <Button
            variant='ghost'
            color='info'
            size='md'
            disabled={isDisabled}
            onClick={() => onClick(mission, participation?.participationId)}
          >
            신청하기
          </Button>
        )}
      </div>
    ),
  };
};

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
    case 'IN_PROGRESS':
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
  onClick: (mission: MissionList) => void,
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
  const formattedDate = new Date(mission.missionDate).toISOString().split('T')[0];

  const isDisabled =
    !!participation && ['SUCCESS', 'FAILED', 'REJECTED'].includes(participation.status);

  return {
    title: mission.title,
    wage: <Lufin size='s' count={mission.wage} />,
    difficulty: difficultyStars,
    participants: `${mission.currentParticipants}/${mission.maxParticipants}`,
    createdAt: formattedDate,
    status: (
      <Badge
        status={
          !status
            ? 'ready'
            : status === 'SUCCESS'
              ? 'done'
              : status === 'IN_PROGRESS'
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
            : status === 'IN_PROGRESS'
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
          status === 'IN_PROGRESS' ? (
            <Button variant='ghost' color='info' size='md' onClick={() => onClick(mission)}>
              리뷰 요청하기
            </Button>
          ) : null
        ) : (
          <Button
            variant='ghost'
            color='info'
            size='md'
            disabled={isDisabled}
            onClick={() => onClick(mission)}
          >
            신청하기
          </Button>
        )}
      </div>
    ),
  };
};

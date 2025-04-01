import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import TableView, { TableRow } from '../../../components/Frame/TableView';
import {
  missionDetails,
  missionParticipations,
  MissionDetail,
} from '../../../types/Mission/mission';
import { Icon } from '../../../components/Icon/Icon';
import Badge from '../../../components/Badge/Badge';
import Button from '../../../components/Button/Button';
import Lufin from '../../../components/Lufin/Lufin';
import { useState } from 'react';
import MyMissionModal from './components/MyMissionModal';

const StudentMission = () => {
  const myMemberId = 1; // TODO: 실제 로그인한 사용자의 member_id로 변경
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedMission, setSelectedMission] = useState<MissionDetail | null>(null);

  // 내가 참여한 미션 데이터
  const myParticipations = missionParticipations.filter(
    (participation) => participation.memberId === myMemberId,
  );

  const myMissionRows = myParticipations
    .map((participation) => {
      const mission = missionDetails.find((m) => m.missionId === participation.missionId);
      if (!mission) return null;

      return {
        title: mission.title,
        wage: <Lufin size='s' count={mission.wage} />,
        difficulty: (
          <div className='flex items-center'>
            {Array(mission.difficulty)
              .fill(0)
              .map((_, i) => (
                <Icon key={i} name='Star' />
              ))}
          </div>
        ),
        participants: `${mission.currentParticipant}/${mission.maxParticipant}`,
        createdAt: new Date(mission.createdAt).toLocaleDateString(),
        status: (
          <Badge
            status={
              participation.status === 'SUCCESS'
                ? 'done'
                : participation.status === 'IN_PROGRESS'
                  ? 'ing'
                  : participation.status === 'CHECKING'
                    ? 'review'
                    : participation.status === 'FAILED'
                      ? 'fail'
                      : 'reject'
            }
          >
            {participation.status === 'SUCCESS'
              ? '성공'
              : participation.status === 'IN_PROGRESS'
                ? '수행 중'
                : participation.status === 'CHECKING'
                  ? '검토 중'
                  : participation.status === 'FAILED'
                    ? '실패'
                    : '거절'}
          </Badge>
        ),
        action: (
          <Button
            variant='ghost'
            color='info'
            size='md'
            disabled={participation.status !== 'IN_PROGRESS'}
            onClick={() => handleRowClick(mission)}
          >
            리뷰 요청하기
          </Button>
        ),
      };
    })
    .filter((row): row is NonNullable<typeof row> => row !== null) as TableRow[];

  // 수행 가능한 미션 데이터
  const availableMissions = missionDetails.filter(
    (mission) =>
      mission.status === 'RECRUITING' &&
      mission.currentParticipant < mission.maxParticipant &&
      !myParticipations.some((p) => p.missionId === mission.missionId),
  );

  const availableMissionRows = availableMissions.map((mission) => ({
    title: mission.title,
    wage: <Lufin size='s' count={mission.wage} />,
    difficulty: (
      <div className='flex items-center'>
        {Array(mission.difficulty)
          .fill(0)
          .map((_, i) => (
            <Icon key={i} name='Star' />
          ))}
      </div>
    ),
    createdAt: new Date(mission.createdAt).toLocaleDateString(),
    participants: `${mission.currentParticipant}/${mission.maxParticipant}`,
    status: <Badge status='ready'>{mission.status === 'RECRUITING' ? '모집 중' : '검토 중'}</Badge>,
    action: (
      <Button variant='ghost' color='info' size='md' onClick={() => handleRowClick(mission)}>
        신청하기
      </Button>
    ),
  }));

  const columns = [
    { key: 'title', label: '미션명' },
    { key: 'wage', label: '보상' },
    { key: 'difficulty', label: '난이도' },
    { key: 'participants', label: '참여인원' },
    { key: 'createdAt', label: '생성일' },
    { key: 'status', label: '상태' },
    { key: 'action', label: '요청' },
  ];

  const handleRowClick = (mission: MissionDetail) => {
    setSelectedMission(mission);
    setIsModalOpen(true);
  };

  return (
    <SidebarLayout>
      <div className='grid grid-rows-2 gap-3'>
        <Card titleLeft='나의 미션' titleSize='l' className='h-[47vh]'>
          <div>
            <TableView columns={columns} rows={myMissionRows} />
          </div>
        </Card>
        <Card titleLeft='수행 가능 미션' titleSize='l' className='h-full flex flex-col'>
          <div>
            <TableView columns={columns} rows={availableMissionRows} />
          </div>
        </Card>
      </div>
      {isModalOpen && selectedMission && (
        <div className='fixed inset-0 flex items-center justify-center z-50'>
          <MyMissionModal
            onClose={() => setIsModalOpen(false)}
            mission={selectedMission}
            participation={myParticipations.find((p) => p.missionId === selectedMission.missionId)}
          />
        </div>
      )}
    </SidebarLayout>
  );
};

export default StudentMission;

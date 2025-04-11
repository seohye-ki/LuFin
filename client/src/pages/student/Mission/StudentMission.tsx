import { useEffect, useState, useCallback } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import TableView from '../../../components/Frame/TableView';
import MyMissionModal from './components/MyMissionModal';
import { useStudentMissions } from './hooks/useStudentMissions';
import useMissionStore from '../../../libs/store/missionStore';
import { MissionList } from '../../../types/mission/mission';

const StudentMission = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const getMissionList = useMissionStore((state) => state.getMissionList);
  const getMissionDetail = useMissionStore((state) => state.getMissionDetail);
  const myMissions = useMissionStore((state) => state.myMissions);
  const availableMissions = useMissionStore((state) => state.availableMissions);
  const selectedMission = useMissionStore((state) => state.selectedMission);

  useEffect(() => {
    getMissionList();
  }, [getMissionList]);

  const handleRowClick = useCallback(
    async (mission: MissionList) => {
      await getMissionDetail(mission.missionId);
      setIsModalOpen(true);
    },
    [getMissionDetail],
  );

  const { myMissionRows, availableMissionRows } = useStudentMissions(
    myMissions.map((mission) => ({
      mission,
      participation: {
        participationId: mission.participationId || 0,
        missionId: mission.missionId,
        status: mission.status as 'RECRUITING' | 'SUCCESS' | 'FAILED' | 'CHECKING' | 'REJECTED',
      },
    })),
    availableMissions,
    handleRowClick,
  );

  const columns = [
    { key: 'title', label: '미션명' },
    { key: 'wage', label: '보상' },
    { key: 'difficulty', label: '난이도' },
    { key: 'participants', label: '참여인원' },
    { key: 'createdAt', label: '날짜' },
    { key: 'status', label: '상태' },
    { key: 'action', label: '요청' },
  ];

  return (
    <SidebarLayout>
      <div className='flex flex-col h-full gap-3'>
        {/* 나의 미션 */}
        <Card titleLeft='나의 미션' titleSize='l' className='flex flex-col basis-50/100 min-h-0'>
          <div>
            <TableView columns={columns} rows={myMissionRows} />
          </div>
        </Card>
        {/* 수행 가능 미션 */}
        <Card
          titleLeft='수행 가능 미션'
          titleSize='l'
          className='flex flex-col basis-50/100 min-h-0'
        >
          <div className='overflow-y-auto flex-1'>
            <TableView columns={columns} rows={availableMissionRows} />
          </div>
        </Card>
      </div>
      {isModalOpen && selectedMission && (
        <>
          <div
            className='fixed inset-0 bg-black z-10'
            style={{ opacity: 0.5 }}
            onClick={() => setIsModalOpen(false)}
          />
          <div className='fixed inset-0 flex items-center justify-center z-40'>
            <MyMissionModal
              mission={selectedMission}
              onClose={() => setIsModalOpen(false)}
              isMyMission={myMissions.some(
                (mission) => mission.missionId === selectedMission.missionId,
              )}
              onSuccess={() => {
                setIsModalOpen(false);
              }}
            />
          </div>
        </>
      )}
    </SidebarLayout>
  );
};

export default StudentMission;

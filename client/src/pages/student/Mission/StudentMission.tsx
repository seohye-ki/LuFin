import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import TableView from '../../../components/Frame/TableView';
import {
  missionDetails,
  missionParticipations,
  MissionDetail,
} from '../../../types/mission/mission';
import { useState } from 'react';
import MyMissionModal from './components/MyMissionModal';
import { useStudentMissions } from './hooks/useStudentMissions';

const StudentMission = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedMission, setSelectedMission] = useState<MissionDetail | null>(null);

  const handleRowClick = (mission: MissionDetail) => {
    setSelectedMission(mission);
    setIsModalOpen(true);
  };

  const { myMissionRows, availableMissionRows } = useStudentMissions(
    1,
    missionDetails,
    missionParticipations,
    handleRowClick,
  );

  const columns = [
    { key: 'title', label: '미션명' },
    { key: 'wage', label: '보상' },
    { key: 'difficulty', label: '난이도' },
    { key: 'participants', label: '참여인원' },
    { key: 'createdAt', label: '생성일' },
    { key: 'status', label: '상태' },
    { key: 'action', label: '요청' },
  ];

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
        <>
          <div
            className='fixed inset-0 bg-black z-10'
            style={{ opacity: 0.5 }}
            onClick={() => setIsModalOpen(false)}
          />
          <div className='fixed inset-0 flex items-center justify-center z-50'>
            <MyMissionModal
              onClose={() => setIsModalOpen(false)}
              mission={selectedMission}
              participation={missionParticipations.find(
                (p) => p.missionId === selectedMission.missionId,
              )}
            />
          </div>
        </>
      )}
    </SidebarLayout>
  );
};

export default StudentMission;

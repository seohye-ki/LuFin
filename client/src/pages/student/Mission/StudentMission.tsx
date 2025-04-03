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
import { Icon } from '../../../components/Icon/Icon';
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
      <div className='flex flex-col h-full gap-3'>
        <Card
          titleLeft='나의 미션'
          titleRight={
            <div className='flex items-center gap-2 bg-broken-white rounded-lg px-4 py-2'>
              <Icon name='SearchNormal1' size={20} color='grey' />
              <input
                type='text'
                placeholder='미션명으로 검색'
                className='bg-transparent outline-none text-p1'
              />
            </div>
          }
          titleSize='l'
          className='flex flex-col basis-50/100 min-h-0'
        >
          <div>
            <TableView columns={columns} rows={myMissionRows} />
          </div>
        </Card>
        <Card
          titleLeft='수행 가능 미션'
          titleRight={
            <div className='flex items-center gap-2 bg-broken-white rounded-lg px-4 py-2'>
              <Icon name='SearchNormal1' size={20} color='grey' />
              <input
                type='text'
                placeholder='미션명으로 검색'
                className='bg-transparent outline-none text-p1'
              />
            </div>
          }
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

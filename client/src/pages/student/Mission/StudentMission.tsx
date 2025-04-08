import { useEffect, useState, useCallback } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Card from '../../../components/Card/Card';
import TableView from '../../../components/Frame/TableView';
import MyMissionModal from './components/MyMissionModal';
import { useStudentMissions } from './hooks/useStudentMissions';
import { Icon } from '../../../components/Icon/Icon';
import useMissionStore from '../../../libs/store/missionStore';
import { MissionList, ParticipationUserInfo } from '../../../types/mission/mission';

const StudentMission = () => {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const getMissionList = useMissionStore((state) => state.getMissionList);
  const getMissionDetail = useMissionStore((state) => state.getMissionDetail);
  const myMissions = useMissionStore((state) => state.myMissions);
  const availableMissions = useMissionStore((state) => state.availableMissions);
  const selectedMission = useMissionStore((state) => state.selectedMission);
  const [participationList, setParticipationList] = useState<ParticipationUserInfo[]>([]);
  const myMemberId = 1; // TODO: 전역 상태로 교체 예정

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

  const { myMissionRows, availableMissionRows, refetchParticipations } = useStudentMissions(
    myMissions,
    availableMissions,
    participationList,
    myMemberId,
    handleRowClick,
    setParticipationList,
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
        {/* 나의 미션 */}
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
        {/* 수행 가능 미션 */}
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
          <div className='fixed inset-0 flex items-center justify-center z-40'>
            <MyMissionModal
              mission={selectedMission}
              onClose={() => setIsModalOpen(false)}
              mode='apply'
              onSuccess={() => {
                refetchParticipations(selectedMission.missionId);
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

import TableView from '../../../../components/Frame/TableView';
import { MissionDetail, missionDetails } from '../../../../types/Mission/mission';
import moment from 'moment';
import { Icon } from '../../../../components/Icon/Icon';
import Badge from '../../../../components/Badge/Badge';
import Lufin from '../../../../components/Lufin/Lufin';
import MissionCreateModal from './MissionCreateModal';
import MissionReadModal from './MissionReadModal';
import { useState } from 'react';

interface WeeklyMissionModalProps {
  selectedDate: Date;
  onDateChange: (date: Date | null) => void;
}

export function WeeklyMissionModal({ selectedDate, onDateChange }: WeeklyMissionModalProps) {
  const [showCreateModal, setShowCreateModal] = useState(false);
  const [selectedMission, setSelectedMission] = useState<MissionDetail | null>(null);
  const startOfWeek = moment(selectedDate).startOf('week');
  const weekDays = Array.from({ length: 7 }, (_, i) => moment(startOfWeek).add(i, 'days'));

  const columns = [
    { key: 'title', label: '이름' },
    { key: 'wage', label: '보상' },
    { key: 'difficulty', label: '난이도' },
    { key: 'participant', label: '인원' },
    { key: 'status', label: '상태' },
  ];

  const dateKey = moment(selectedDate).format('YYYY-MM-DD');
  const dayMissions = missionDetails.filter((mission) => mission.missionDate === dateKey);

  const rows = dayMissions.map((mission) => ({
    title: mission.title,
    wage: <Lufin size='s' count={mission.wage} />,
    difficulty: Array(mission.difficulty)
      .fill(0)
      .map((_, i) => <Icon key={i} name='Star' />),
    participant: `${mission.currentParticipant}/${mission.maxParticipant}`,
    status:
      mission.status === 'RECRUITING' ? (
        <Badge status='ing'>모집중</Badge>
      ) : (
        <Badge status='done'>모집완료</Badge>
      ),
  }));

  return (
    <div className='w-full h-[calc(100vh-32px)] rounded-lg p-4 bg-white'>
      <div className='flex justify-center mb-4 items-center'>
        <button
          className='flex justify-center items-center gap-2 hover:bg-light-cyan-30 rounded-full p-2'
          onClick={() => onDateChange(null)}
        >
          <Icon name='ArrowDown2' size={24} />
        </button>
      </div>
      <div className='grid grid-cols-7 rounded-lg bg-light-cyan-30'>
        {weekDays.map((day, index) => (
          <div
            key={day.format('YYYY-MM-DD')}
            onClick={() => onDateChange(day.toDate())}
            className={`text-center p-2 ${
              day.format('YYYY-MM-DD') === dateKey ? 'bg-light-cyan rounded-full font-bold' : ''
            }`}
          >
            <div
              className={`text-sm mb-1 ${
                index === 0 ? 'text-red-500' : index === 6 ? 'text-blue-500' : ''
              }`}
            >
              {day.format('ddd')}
            </div>
            <div className='text-lg'>{day.format('D')}</div>
          </div>
        ))}
      </div>
      <div className='p-4'>
        {/* 해당 날짜 */}
        <div className='flex justify-between items-center text-h3 mb-4 font-semibold'>
          {moment(selectedDate).format('YYYY년 MM월 DD일')}
          <button
            className='flex items-center justify-center'
            onClick={() => setShowCreateModal(true)}
          >
            <Icon name='CircleAdd' size={32} />
          </button>
        </div>
        <TableView
          columns={columns}
          rows={rows}
          onRowClick={(row) => {
            const mission = dayMissions.find((m) => m.title === row.title);
            if (mission) {
              setSelectedMission(mission);
            }
          }}
        />
        {showCreateModal && <MissionCreateModal onClose={() => setShowCreateModal(false)} />}
        {selectedMission && (
          <MissionReadModal mission={selectedMission} onClose={() => setSelectedMission(null)} />
        )}
      </div>
    </div>
  );
}

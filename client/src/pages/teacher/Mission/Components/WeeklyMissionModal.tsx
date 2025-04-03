import { missionDetails } from '../../../../types/mission/mission';
import moment from 'moment';
import { Icon } from '../../../../components/Icon/Icon';
import MissionTable from './MissionTable';
import MissionCreateModal from './MissionCreateModal';
import MissionReadModal from './MissionReadModal';
import { useWeeklyMissionModal } from '../hooks/useWeeklyMissionModal';
interface WeeklyMissionModalProps {
  selectedDate: Date;
  onDateChange: (date: Date | null) => void;
}

export function WeeklyMissionModal({ selectedDate, onDateChange }: WeeklyMissionModalProps) {
  const { showCreateModal, selectedMission, onSelectMission, onCloseModal, setShowCreateModal } =
    useWeeklyMissionModal();

  const startOfWeek = moment(selectedDate).startOf('week');
  const weekDays = Array.from({ length: 7 }, (_, i) => moment(startOfWeek).add(i, 'days'));

  const dateKey = moment(selectedDate).format('YYYY-MM-DD');
  const dayMissions = missionDetails.filter((mission) => mission.missionDate === dateKey);

  return (
    <div className='w-full max-h-full overflow-y-auto rounded-lg p-4 bg-white'>
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
              day.format('YYYY-MM-DD') === dateKey ? 'bg-light-cyan rounded-lg font-bold' : ''
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
        <MissionTable missions={dayMissions} onMissionClick={onSelectMission} />
        {showCreateModal && <MissionCreateModal onClose={() => setShowCreateModal(false)} />}
        {selectedMission && <MissionReadModal mission={selectedMission} onClose={onCloseModal} />}
      </div>
    </div>
  );
}

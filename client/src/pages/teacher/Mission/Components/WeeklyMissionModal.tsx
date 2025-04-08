import { Icon } from '../../../../components/Icon/Icon';
import MissionTable from './MissionTable';
import MissionCreateModal from './MissionCreateModal';
import MissionReadModal from './MissionReadModal';
import { useWeeklyMissionModal } from '../hooks/useWeeklyMissionModal';
import useMissionStore from '../../../../libs/store/missionStore';

interface WeeklyMissionModalProps {
  selectedDate: Date;
  onDateChange: (date: Date | null) => void;
}

// 요일 이름 반환 (일~토)
const getWeekdayName = (index: number) => {
  const weekdays = ['일', '월', '화', '수', '목', '금', '토'];
  return weekdays[index];
};

const formatDateKey = (date: Date) => date.toISOString().split('T')[0];

export function WeeklyMissionModal({ selectedDate, onDateChange }: WeeklyMissionModalProps) {
  const { showCreateModal, selectedMission, onSelectMission, onCloseModal, setShowCreateModal } =
    useWeeklyMissionModal();

  // 일요일 기준으로 주 시작 날짜 구하기
  const startOfWeek = new Date(selectedDate);
  startOfWeek.setDate(startOfWeek.getDate() - startOfWeek.getDay());

  // 일~토까지 날짜 배열 생성
  const weekDays = Array.from({ length: 7 }, (_, i) => {
    const day = new Date(startOfWeek);
    day.setDate(day.getDate() + i);
    return day;
  });

  const missions = useMissionStore((state) => state.myMissions);

  const dateKey = formatDateKey(selectedDate);
  const dayMissions = missions.filter(
    (mission) => formatDateKey(new Date(mission.missionDate)) === dateKey,
  );

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

      {/* 요일 그리드 */}
      <div className='grid grid-cols-7 rounded-lg bg-light-cyan-30'>
        {weekDays.map((day) => {
          const dayKey = formatDateKey(day);
          const isSelected = dayKey === dateKey;
          const dayOfWeek = day.getDay();

          return (
            <div
              key={dayKey}
              onClick={() => onDateChange(day)}
              className={`text-center p-2 ${isSelected ? 'bg-light-cyan rounded-lg font-bold' : ''}`}
            >
              <div
                className={`text-sm mb-1 ${
                  dayOfWeek === 0 ? 'text-red-500' : dayOfWeek === 6 ? 'text-blue-500' : ''
                }`}
              >
                {getWeekdayName(dayOfWeek)}
              </div>
              <div className='text-lg'>{day.getDate()}</div>
            </div>
          );
        })}
      </div>

      <div className='p-4'>
        {/* 선택 날짜 헤더 */}
        <div className='flex justify-between items-center text-h3 mb-4 font-semibold'>
          {`${selectedDate.getFullYear()}년 ${selectedDate.getMonth() + 1}월 ${selectedDate.getDate()}일`}
          <button
            className='flex items-center justify-center'
            onClick={() => setShowCreateModal(true)}
          >
            <Icon name='CircleAdd' size={32} />
          </button>
        </div>

        <MissionTable missions={dayMissions} onMissionClick={onSelectMission} />

        {showCreateModal && (
          <MissionCreateModal
            selectedDate={selectedDate}
            onClose={() => setShowCreateModal(false)}
          />
        )}
        {selectedMission && <MissionReadModal mission={selectedMission} onClose={onCloseModal} />}
      </div>
    </div>
  );
}

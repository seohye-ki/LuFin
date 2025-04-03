import { useState } from 'react';
import { Icon } from '../../../components/Icon/Icon';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassroomCard from '../../teacher/Classroom/components/ClassroomCard';
import Button from '../../../components/Button/Button';
import JoinClassroomModal from './components/JoinClassroomModal';
import useAlertStore from '../../../libs/store/alertStore';

const StudentClassroom = () => {
  const [isJoinModalOpen, setIsJoinModalOpen] = useState(false);
  const [invalidCode, setInvalidCode] = useState<string | null>(null);

  const classrooms = [
    {
      id: 1,
      title: '꿈나무 열정 교실',
      school: '문이차초등학교',
      grade: '5학년 2반',
      students: 21,
      year: 2024,
    },
    {
      id: 2,
      title: '미래 금융 경제 교실',
      school: '미래초등학교',
      grade: '5학년 4반',
      students: 15,
      year: 2023,
    },
    // ... more classrooms
  ];

  const handleJoinSubmit = (code: string) => {
    console.log('Joining classroom with code:', code);

    // 테스트를 위한 모의 API 호출
    setTimeout(() => {
      if (code === '11111') {
        // 성공 케이스 - 모달 닫기 및 성공 알림
        setIsJoinModalOpen(false);
        useAlertStore
          .getState()
          .showAlert('클래스룸 참여 완료', null, '꿈나무 열정 교실에 참여했습니다.', 'success', {
            label: '확인',
            onClick: () => useAlertStore.getState().hideAlert(),
            color: 'primary',
          });
      } else {
        // 실패 케이스 - 존재하지 않는 코드
        setInvalidCode(code);
      }
    }, 1000);
  };

  // 모달 닫기 시 상태 초기화
  const handleCloseModal = () => {
    setIsJoinModalOpen(false);
    setInvalidCode(null);
  };

  // 코드 변경 시 상태 초기화
  const handleCodeChange = () => {
    if (invalidCode) {
      setInvalidCode(null);
    }
  };

  return (
    <SidebarLayout>
      <div className='h-full flex flex-col gap-6 overflow-y-auto'>
        <div className='flex justify-between items-center'>
          <h1 className='text-h2 font-semibold'>클래스룸</h1>
          <div className='flex items-center gap-4'>
            <div className='flex items-center gap-2 bg-white rounded-lg px-4 py-2'>
              <Icon name='SearchNormal1' size={20} color='grey' />
              <input
                type='text'
                placeholder='제목 또는 작성자로 검색'
                className='bg-transparent outline-none text-p1'
              />
            </div>
            <Button
              color='info'
              variant='solid'
              size='md'
              className='flex items-center gap-2'
              onClick={() => setIsJoinModalOpen(true)}
            >
              <Icon name='AddCircle' size={20} color='white' />
              <span>새 클래스 참여</span>
            </Button>
          </div>
        </div>

        <div className='grid grid-cols-3 gap-4'>
          {classrooms.map((classroom) => (
            <ClassroomCard
              key={classroom.id}
              {...classroom}
              showMenu={false}
              onClick={() => {
                // Handle classroom entry
              }}
            />
          ))}
        </div>

        {isJoinModalOpen && (
          <JoinClassroomModal
            onClose={handleCloseModal}
            onSubmit={handleJoinSubmit}
            isCodeInvalid={!!invalidCode}
            onInvalidCode={() => console.log('Invalid code:', invalidCode)}
            onCodeChange={handleCodeChange}
          />
        )}
      </div>
    </SidebarLayout>
  );
};

export default StudentClassroom;

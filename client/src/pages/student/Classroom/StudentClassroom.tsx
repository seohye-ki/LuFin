import { useEffect, useState } from 'react';
import { Icon } from '../../../components/Icon/Icon';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassroomCard from '../../teacher/Classroom/components/ClassroomCard';
import Button from '../../../components/Button/Button';
import JoinClassroomModal from './components/JoinClassroomModal';
import useAlertStore from '../../../libs/store/alertStore';
import Alert from '../../../components/Alert/Alert';
import useClassroomStore from '../../../libs/store/classroomStore';
import { useNavigate } from 'react-router-dom';

const StudentClassroom = () => {
  const navigate = useNavigate();
  const [isJoinModalOpen, setIsJoinModalOpen] = useState(false);
  const [isCodeEntryModalOpen, setIsCodeEntryModalOpen] = useState(false);
  const { 
    classrooms, 
    isLoading, 
    error, 
    fetchClassrooms, 
    joinClassroom, 
    enterClassCode, 
    changeCurrentClass 
  } = useClassroomStore();

  useEffect(() => {
    fetchClassrooms();
  }, [fetchClassrooms]);

  const handleJoinSubmit = async (inviteCode: string) => {
    try {
      await joinClassroom(inviteCode);
      setIsJoinModalOpen(false);

      useAlertStore
        .getState()
        .showAlert('클래스룸 참여 완료', null, '클래스룸에 참여했습니다.', 'success', {
          label: '확인',
          onClick: () => {
            useAlertStore.getState().hideAlert();
            // 클래스룸 참여 후 학생 대시보드로 이동
            navigate('/dashboard');
          },
          color: 'primary',
        });
    } catch {
      useAlertStore
        .getState()
        .showAlert('클래스룸 참여 실패', null, '클래스룸 참여에 실패했습니다.', 'danger', {
          label: '확인',
          onClick: () => useAlertStore.getState().hideAlert(),
          color: 'primary',
        });
    }
  };
  
  const handleCodeEntrySubmit = async (code: string) => {
    try {
      await enterClassCode(code);
      setIsCodeEntryModalOpen(false);

      useAlertStore
        .getState()
        .showAlert('클래스 코드 입력 완료', null, '클래스 코드가 성공적으로 적용되었습니다.', 'success', {
          label: '확인',
          onClick: () => {
            useAlertStore.getState().hideAlert();
          },
          color: 'primary',
        });
    } catch {
      useAlertStore
        .getState()
        .showAlert('클래스 코드 입력 실패', null, '클래스 코드 입력에 실패했습니다.', 'danger', {
          label: '확인',
          onClick: () => useAlertStore.getState().hideAlert(),
          color: 'primary',
        });
    }
  };

  const handleClassroomClick = (classId: number) => {
    // 클래스룸 선택 시 현재 클래스 정보 저장
    const selectedClassroom = classrooms.find(classroom => classroom.classId === classId);
    if (selectedClassroom) {
      changeCurrentClass(classId, selectedClassroom.name);
    }
    // 대시보드로 이동
    navigate('/dashboard');
  };

  return (
    <SidebarLayout userRole='student' hideMenu>
      <div className='h-full flex flex-col gap-6 overflow-y-auto p-6'>
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
              onClick={() => setIsCodeEntryModalOpen(true)}
              className='flex items-center gap-2'
            >
              <Icon name='AddCircle' size={20} color='white' />
              <span>클래스 코드 입력</span>
            </Button>
          </div>
        </div>

        {error && <div className='text-error text-p2 bg-error/10 p-4 rounded-lg'>{error}</div>}

        <div className='grid grid-cols-3 gap-4'>
          {classrooms.map((classroom) => (
            <ClassroomCard
              key={classroom.classId}
              id={classroom.classId}
              title={classroom.name}
              school={classroom.school}
              grade={`${classroom.grade}학년 ${classroom.classGroup}반`}
              students={classroom.memberCount}
              year={classroom.year}
              imageKey={classroom.key}
              onClick={() => handleClassroomClick(classroom.classId)}
            />
          ))}
        </div>

        {isJoinModalOpen && (
          <JoinClassroomModal
            onClose={() => {
              if (!isLoading) setIsJoinModalOpen(false);
            }}
            onSubmit={handleJoinSubmit}
            isLoading={isLoading}
          />
        )}
        
        {isCodeEntryModalOpen && (
          <JoinClassroomModal
            onClose={() => {
              if (!isLoading) setIsCodeEntryModalOpen(false);
            }}
            onSubmit={() => {}}
            onEnterCode={handleCodeEntrySubmit}
            isLoading={isLoading}
            mode="enterCode"
            title="클래스 코드 입력"
            description="선생님이 제공한 클래스 코드를 입력해주세요"
          />
        )}

        <Alert />
      </div>
    </SidebarLayout>
  );
};

export default StudentClassroom;

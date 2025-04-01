import { useState } from 'react';
import { Icon } from '../../../components/Icon/Icon';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassroomCard from './components/ClassroomCard';
import CreateClassroomModal from './components/CreateClassroomModal';
import Button from '../../../components/Button/Button';
import useAlertStore from '../../../libs/store/alertStore';
import Alert from '../../../components/Alert/Alert';

const TeacherClassroom = () => {
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [classrooms, setClassrooms] = useState([
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
  ]);

  const handleEdit = (id: number) => {
    console.log(`Edit classroom ${id}`);
    // TODO: 수정 로직 구현
  };

  const handleDelete = (id: number) => {
    console.log(`Delete classroom ${id}`);
    // TODO: 삭제 로직 구현
  };

  const handleCreateSubmit = (data: {
    title: string;
    school: string;
    grade: number;
    class: number;
    image?: File;
  }) => {
    console.log('Create classroom:', data);

    // 로딩 상태 시작
    setIsLoading(true);

    // 모의 API 호출 (실제로는 API 호출 코드로 대체)
    setTimeout(() => {
      // 새 클래스룸 객체 생성
      const newClassroom = {
        id: Date.now(), // 임시 ID
        title: data.title,
        school: data.school,
        grade: `${data.grade}학년 ${data.class}반`,
        students: 0,
        year: new Date().getFullYear(),
      };

      // 클래스룸 목록에 추가
      setClassrooms([...classrooms, newClassroom]);

      // 로딩 상태 종료 및 모달 닫기
      setIsLoading(false);
      setIsCreateModalOpen(false);

      // 클래스룸 생성 성공 알림 표시
      useAlertStore
        .getState()
        .showAlert(
          '클래스룸 생성 완료',
          null,
          `${data.title} 클래스룸이 생성되었습니다.`,
          'success',
          {
            label: '확인',
            onClick: () => useAlertStore.getState().hideAlert(),
            color: 'primary',
          },
        );
    }, 1500); // 1.5초 후 생성 완료 (실제로는 API 응답 시간에 따라 달라짐)
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
              onClick={() => setIsCreateModalOpen(true)}
              className='flex items-center gap-2'
            >
              <Icon name='AddCircle' size={20} color='white' />
              <span>새 클래스 생성</span>
            </Button>
          </div>
        </div>

        <div className='grid grid-cols-3 gap-4'>
          {classrooms.map((classroom) => (
            <ClassroomCard
              key={classroom.id}
              {...classroom}
              onClick={() => console.log(`Clicked classroom ${classroom.id}`)}
              onEdit={() => handleEdit(classroom.id)}
              onDelete={() => handleDelete(classroom.id)}
            />
          ))}
        </div>

        {isCreateModalOpen && (
          <CreateClassroomModal
            onClose={() => {
              if (!isLoading) setIsCreateModalOpen(false);
            }}
            onSubmit={handleCreateSubmit}
            isLoading={isLoading}
          />
        )}

        {/* Alert 컴포넌트 추가 */}
        <Alert />
      </div>
    </SidebarLayout>
  );
};

export default TeacherClassroom;

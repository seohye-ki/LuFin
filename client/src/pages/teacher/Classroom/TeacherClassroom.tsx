import { useEffect, useState } from 'react';
import { Icon } from '../../../components/Icon/Icon';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassroomCard from './components/ClassroomCard';
import CreateClassroomModal from './components/CreateClassroomModal';
import EditClassroomModal from './components/EditClassroomModal';
import Button from '../../../components/Button/Button';
import useAlertStore from '../../../libs/store/alertStore';
import Alert from '../../../components/Alert/Alert';
import useClassroomStore from '../../../libs/store/classroomStore';
import {
  CreateClassroomRequest,
  UpdateClassroomRequest,
  Classroom,
} from '../../../libs/services/classroom/classroomService';
import { useNavigate } from 'react-router-dom';

const TeacherClassroom = () => {
  const navigate = useNavigate();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [editingClassroom, setEditingClassroom] = useState<Classroom | null>(null);
  const {
    classrooms,
    isLoading,
    error,
    fetchClassrooms,
    createClassroom,
    updateClassroom,
    deleteClassroom,
  } = useClassroomStore();

  useEffect(() => {
    fetchClassrooms();
  }, [fetchClassrooms]);

  const handleEdit = (id: number) => {
    const classroom = classrooms.find((c) => c.classId === id);
    if (classroom) {
      setEditingClassroom(classroom);
    }
  };

  const handleDelete = async (id: number) => {
    useAlertStore.getState().showAlert(
      '클래스룸 삭제',
      null,
      '정말 이 클래스룸을 삭제하시겠습니까?',
      'warning',
      {
        label: '취소',
        onClick: () => useAlertStore.getState().hideAlert(),
        color: 'neutral',
      },
      {
        label: '삭제',
        onClick: async () => {
          try {
            await deleteClassroom(id);
            useAlertStore
              .getState()
              .showAlert('클래스룸 삭제 완료', null, '클래스룸이 삭제되었습니다.', 'success', {
                label: '확인',
                onClick: () => useAlertStore.getState().hideAlert(),
                color: 'primary',
              });
          } catch {
            // 에러는 store에서 처리됨
          }
        },
        color: 'danger',
      },
    );
  };

  const handleCreateSubmit = async (data: {
    title: string;
    school: string;
    grade: number;
    class: number;
    image?: File;
    key?: string | null;
  }) => {
    try {
      const createData: CreateClassroomRequest = {
        name: data.title,
        school: data.school,
        grade: data.grade,
        classGroup: data.class,
        key: data.key ?? null,
      };

      await createClassroom(createData);
      setIsCreateModalOpen(false);

      useAlertStore
        .getState()
        .showAlert(
          '클래스룸 생성 완료',
          null,
          `${data.title} 클래스룸이 생성되었습니다.`,
          'success',
          {
            label: '확인',
            onClick: () => {
              useAlertStore.getState().hideAlert();
              // 클래스룸 생성 후 교사 대시보드로 이동
              navigate('/dashboard/teacher');
            },
            color: 'primary',
          },
        );
    } catch {
      useAlertStore
        .getState()
        .showAlert('클래스룸 생성 실패', null, '클래스룸 생성에 실패했습니다.', 'danger', {
          label: '확인',
          onClick: () => useAlertStore.getState().hideAlert(),
          color: 'primary',
        });
    }
  };

  const handleEditSubmit = async (data: {
    classId: number;
    title: string;
    school: string;
    grade: number;
    class: number;
    image?: File;
    key?: string | null;
  }) => {
    try {
      const updateData: UpdateClassroomRequest = {
        classId: data.classId,
        name: data.title,
        school: data.school,
        grade: data.grade,
        classGroup: data.class,
        key: data.key ?? null,
      };

      await updateClassroom(updateData);
      setEditingClassroom(null);

      useAlertStore
        .getState()
        .showAlert(
          '클래스룸 수정 완료',
          null,
          `${data.title} 클래스룸이 수정되었습니다.`,
          'success',
          {
            label: '확인',
            onClick: () => useAlertStore.getState().hideAlert(),
            color: 'primary',
          },
        );
    } catch {
      useAlertStore
        .getState()
        .showAlert('클래스룸 수정 실패', null, '클래스룸 수정에 실패했습니다.', 'danger', {
          label: '확인',
          onClick: () => useAlertStore.getState().hideAlert(),
          color: 'primary',
        });
    }
  };

  const handleClassroomClick = () => {
    // 클래스룸 클릭 시 교사 대시보드로 이동
    navigate('/dashboard/teacher');
  };

  return (
    <SidebarLayout userRole='teacher' hideMenu>
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
              onClick={() => setIsCreateModalOpen(true)}
              className='flex items-center gap-2'
            >
              <Icon name='AddCircle' size={20} color='white' />
              <span>새 클래스 생성</span>
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
              onClick={() => handleClassroomClick()}
              onEdit={() => handleEdit(classroom.classId)}
              onDelete={() => handleDelete(classroom.classId)}
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

        {editingClassroom && (
          <EditClassroomModal
            classroom={editingClassroom}
            onClose={() => {
              if (!isLoading) setEditingClassroom(null);
            }}
            onSubmit={handleEditSubmit}
            isLoading={isLoading}
          />
        )}

        <Alert />
      </div>
    </SidebarLayout>
  );
};

export default TeacherClassroom;

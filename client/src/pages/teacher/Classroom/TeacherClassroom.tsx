import { useEffect, useState } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassroomCard from './components/ClassroomCard';
import CreateClassroomModal from './components/CreateClassroomModal';
import EditClassroomModal from './components/EditClassroomModal';
import Button from '../../../components/Button/Button';
import useAlertStore from '../../../libs/store/alertStore';
import Alert from '../../../components/Alert/Alert';
import {
  CreateClassroomRequest,
  UpdateClassroomRequest,
  Classroom,
  classroomService,
} from '../../../libs/services/classroom/classroomService';
import { useNavigate } from 'react-router-dom';
import useClassroomStore from '../../../libs/store/classroomStore';
import Card from '../../../components/Card/Card';
import useAuthStore from '../../../libs/store/authStore';
import JoinClassroomModal from './components/JoinClassroomModal';

const TeacherClassroom = () => {
  const navigate = useNavigate();
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false);
  const [isEnterClassroomModalOpen, setIsEnterClassroomModalOpen] = useState(false);
  const [editingClassroom, setEditingClassroom] = useState<Classroom | null>(null);
  const [classrooms, setClassrooms] = useState<Classroom[]>([]);
  const { setCurrentClass } = useClassroomStore();
  const { userRole } = useAuthStore();

  const fetchClassrooms = async () => {
    const res = await classroomService.getClassrooms();
    setClassrooms(res.data);
  };

  const deleteClassroom = async (classroomId: number) => {
    await classroomService.deleteClassroom(classroomId);
  };

  const createClassroom = async (createClassroomDTO: CreateClassroomRequest) => {
    await classroomService.createClassroom(createClassroomDTO);
  };

  const updateClassroom = async (updateClassroomDTO: UpdateClassroomRequest) => {
    await classroomService.updateClassroom(updateClassroomDTO);
  };

  useEffect(() => {
    fetchClassrooms();
  }, []);

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
          await deleteClassroom(id);
          useAlertStore
            .getState()
            .showAlert('클래스룸 삭제 완료', null, '클래스룸이 삭제되었습니다.', 'success', {
              label: '확인',
              onClick: () => useAlertStore.getState().hideAlert(),
              color: 'primary',
            });
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
            navigate('/dashboard');
          },
          color: 'primary',
        },
      );
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
  };

  const handleClassroomClick = async (classroom: Classroom) => {
    setCurrentClass(classroom.classId, classroom.name, classroom.code);
    navigate('/dashboard');
  };

  return (
    <SidebarLayout>
      <Card
        className='h-full flex flex-col gap-6 overflow-y-auto p-6'
        titleLeft='클래스 목록'
        titleRight={
          <Button
            color='info'
            variant='solid'
            size='md'
            onClick={() => {
              return userRole === 'TEACHER'
                ? setIsCreateModalOpen(true)
                : setIsEnterClassroomModalOpen(true);
            }}
            className='flex items-center gap-2'
          >
            <span>{userRole === 'TEACHEAR' ? '클래스 추가' : '클래스 입장'}</span>
          </Button>
        }
      >
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
              isTeacher={true}
              code={classroom.code}
              onClick={() => handleClassroomClick(classroom)}
              onEdit={() => handleEdit(classroom.classId)}
              onDelete={() => handleDelete(classroom.classId)}
            />
          ))}
        </div>

        {isCreateModalOpen && (
          <CreateClassroomModal
            onClose={() => setIsCreateModalOpen(false)}
            onSubmit={handleCreateSubmit}
          />
        )}

        {isEnterClassroomModalOpen && (
          <JoinClassroomModal onClose={() => setIsEnterClassroomModalOpen(false)} />
        )}

        {editingClassroom && (
          <EditClassroomModal
            classroom={editingClassroom}
            onClose={() => setEditingClassroom(null)}
            onSubmit={handleEditSubmit}
          />
        )}

        <Alert />
      </Card>
    </SidebarLayout>
  );
};

export default TeacherClassroom;

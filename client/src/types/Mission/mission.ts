export interface Mission {
  mission_id: number;
  class_id: number;
  title: string;
  content: string;
  difficulty: number;
  maxParticipant: number;
  currentParticipant: number;
  wage: number;
  mission_date: string; // 미션 수행일
  status: 'RECRUITING' | 'IN_PROGRESS' | 'CLOSED';
  createdAt: string; // 생성일
  updatedAt: string; // 수정일
}

export interface MissionImage {
  mission_image_id: number;
  mission_id: number;
  image_url: string;
}

export interface MissionParticipation {
  participation_id: number;
  mission_id: number;
  member_id: number;
  status: 'IN_PROGRESS' | 'CHECKING' | 'SUCCESS' | 'FAILED' | 'REJECTED';
  wage_status: boolean;
  reject_count: number;
  createdAt: string;
  updatedAt: string;
}

export interface MissionDetail extends Mission {
  mission_images: MissionImage[];
  mission_participations: MissionParticipation[];
}

export const missions: Mission[] = [
  {
    mission_id: 1,
    class_id: 1,
    title: '교실 청소하기',
    content: '교실을 청소하세요.',
    difficulty: 1,
    maxParticipant: 2,
    currentParticipant: 1,
    wage: 2000,
    mission_date: '2025-03-20',
    status: 'CLOSED',
    createdAt: '2025-03-20',
    updatedAt: '2025-03-20',
  },
  {
    mission_id: 2,
    class_id: 1,
    title: '안내문 정리하기',
    content: '안내문을 정리하고 친구들에게 1장씩 나눠주세요',
    wage: 3000,
    difficulty: 1,
    maxParticipant: 2,
    currentParticipant: 1,
    mission_date: '2025-03-20',
    status: 'RECRUITING',
    createdAt: '2025-03-20',
    updatedAt: '2025-03-20',
  },
  {
    mission_id: 3,
    class_id: 1,
    title: '칠판 정리하기',
    content: '칠판을 정리하세요.',
    wage: 1000,
    difficulty: 3,
    maxParticipant: 2,
    currentParticipant: 1,
    mission_date: '2025-03-21',
    status: 'RECRUITING',
    createdAt: '2025-03-21',
    updatedAt: '2025-03-21',
  },
  {
    mission_id: 4,
    class_id: 1,
    title: '교실 환경 정리하기',
    content: '교실 환경을 깔끔하게 정리하세요.',
    wage: 2500,
    difficulty: 2,
    maxParticipant: 2,
    currentParticipant: 2,
    mission_date: '2025-03-21',
    status: 'IN_PROGRESS',
    createdAt: '2025-03-21',
    updatedAt: '2025-03-21',
  },
];

export const missionImages: MissionImage[] = [
  {
    mission_image_id: 1,
    mission_id: 1,
    image_url: 'https://picsum.photos/200/300?random=1',
  },
  {
    mission_image_id: 2,
    mission_id: 1,
    image_url: 'https://picsum.photos/200/300?random=2',
  },
  {
    mission_image_id: 3,
    mission_id: 1,
    image_url: 'https://picsum.photos/200/300?random=3',
  },
  {
    mission_image_id: 4,
    mission_id: 2,
    image_url: 'https://picsum.photos/200/300?random=4',
  },
  {
    mission_image_id: 5,
    mission_id: 2,
    image_url: 'https://picsum.photos/200/300?random=5',
  },
  {
    mission_image_id: 6,
    mission_id: 2,
    image_url: 'https://picsum.photos/200/300?random=6',
  },
];

export const missionParticipations: MissionParticipation[] = [
  {
    participation_id: 1,
    mission_id: 1,
    member_id: 1,
    status: 'SUCCESS',
    wage_status: true,
    reject_count: 0,
    createdAt: '2025-03-20',
    updatedAt: '2025-03-20',
  },
  {
    participation_id: 2,
    mission_id: 1,
    member_id: 2,
    status: 'IN_PROGRESS',
    wage_status: false,
    reject_count: 0,
    createdAt: '2025-03-20',
    updatedAt: '2025-03-20',
  },
  {
    participation_id: 3,
    mission_id: 2,
    member_id: 3,
    status: 'CHECKING',
    wage_status: false,
    reject_count: 1,
    createdAt: '2025-03-20',
    updatedAt: '2025-03-20',
  },
  {
    participation_id: 4,
    mission_id: 4,
    member_id: 1,
    status: 'IN_PROGRESS',
    wage_status: false,
    reject_count: 0,
    createdAt: '2025-03-21',
    updatedAt: '2025-03-21',
  },
];

export const missionDetails: MissionDetail[] = missions.map((mission) => ({
  ...mission,
  mission_images: missionImages.filter((image) => image.mission_id === mission.mission_id),
  mission_participations: missionParticipations.filter(
    (participation) => participation.mission_id === mission.mission_id,
  ),
}));

export interface Mission {
  missionId: number;
  classId: number;
  title: string;
  content: string;
  difficulty: number;
  maxParticipant: number;
  currentParticipant: number;
  wage: number;
  missionDate: string;
  status: 'RECRUITING' | 'IN_PROGRESS' | 'CLOSED';
  createdAt: string;
  updatedAt: string;
}

export interface MissionImage {
  missionImageId: number;
  missionId: number;
  imageUrl: string;
}

export interface MissionParticipation {
  participationId: number;
  missionId: number;
  memberId: number;
  status: 'IN_PROGRESS' | 'CHECKING' | 'SUCCESS' | 'FAILED' | 'REJECTED';
  wageStatus: boolean;
  rejectCount: number;
  createdAt: string;
  updatedAt: string;
}

export interface MissionDetail extends Mission {
  missionImages: MissionImage[];
  missionParticipations: MissionParticipation[];
}

export const missions: Mission[] = [
  {
    missionId: 1,
    classId: 1,
    title: '교실 청소하기',
    content: '교실을 청소하세요.',
    difficulty: 1,
    maxParticipant: 2,
    currentParticipant: 1,
    wage: 2000,
    missionDate: '2025-04-20',
    status: 'CLOSED',
    createdAt: '2025-04-20',
    updatedAt: '2025-04-20',
  },
  {
    missionId: 2,
    classId: 1,
    title: '안내문 정리하기',
    content: '안내문을 정리하고 친구들에게 1장씩 나눠주세요',
    difficulty: 1,
    maxParticipant: 2,
    currentParticipant: 1,
    wage: 3000,
    missionDate: '2025-04-20',
    status: 'RECRUITING',
    createdAt: '2025-04-20',
    updatedAt: '2025-04-20',
  },
  {
    missionId: 3,
    classId: 1,
    title: '칠판 정리하기',
    content: '칠판을 정리하세요.',
    difficulty: 3,
    maxParticipant: 2,
    currentParticipant: 1,
    wage: 1000,
    missionDate: '2025-04-21',
    status: 'RECRUITING',
    createdAt: '2025-04-21',
    updatedAt: '2025-04-21',
  },
  {
    missionId: 4,
    classId: 1,
    title: '교실 환경 정리하기',
    content: '교실 환경을 깔끔하게 정리하세요.',
    difficulty: 2,
    maxParticipant: 2,
    currentParticipant: 2,
    wage: 2500,
    missionDate: '2025-04-21',
    status: 'IN_PROGRESS',
    createdAt: '2025-04-21',
    updatedAt: '2025-04-21',
  },
];

export const missionImages: MissionImage[] = [
  { missionImageId: 1, missionId: 1, imageUrl: 'https://picsum.photos/200/300?random=1' },
  { missionImageId: 2, missionId: 1, imageUrl: 'https://picsum.photos/200/300?random=2' },
  { missionImageId: 3, missionId: 1, imageUrl: 'https://picsum.photos/200/300?random=3' },
  { missionImageId: 4, missionId: 2, imageUrl: 'https://picsum.photos/200/300?random=4' },
  { missionImageId: 5, missionId: 2, imageUrl: 'https://picsum.photos/200/300?random=5' },
  { missionImageId: 6, missionId: 2, imageUrl: 'https://picsum.photos/200/300?random=6' },
  { missionImageId: 7, missionId: 3, imageUrl: 'https://picsum.photos/200/300?random=7' },
];

export const missionParticipations: MissionParticipation[] = [
  {
    participationId: 1,
    missionId: 1,
    memberId: 1,
    status: 'SUCCESS',
    wageStatus: true,
    rejectCount: 0,
    createdAt: '2025-04-20',
    updatedAt: '2025-04-20',
  },
  {
    participationId: 2,
    missionId: 1,
    memberId: 2,
    status: 'IN_PROGRESS',
    wageStatus: false,
    rejectCount: 0,
    createdAt: '2025-04-20',
    updatedAt: '2025-04-20',
  },
  {
    participationId: 3,
    missionId: 2,
    memberId: 3,
    status: 'CHECKING',
    wageStatus: false,
    rejectCount: 1,
    createdAt: '2025-04-20',
    updatedAt: '2025-04-20',
  },
  {
    participationId: 4,
    missionId: 4,
    memberId: 1,
    status: 'IN_PROGRESS',
    wageStatus: true,
    rejectCount: 0,
    createdAt: '2025-04-21',
    updatedAt: '2025-04-21',
  },
];

export const missionDetails: MissionDetail[] = missions.map((mission) => ({
  ...mission,
  missionImages: missionImages.filter((image) => image.missionId === mission.missionId),
  missionParticipations: missionParticipations.filter(
    (participation) => participation.missionId === mission.missionId,
  ),
}));

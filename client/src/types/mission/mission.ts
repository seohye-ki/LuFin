export interface Mission {
  missionId: number;
  classId: number;
  title: string;
  content: string;
  difficulty: number;
  maxParticipants: number;
  currentParticipants: number;
  wage: number;
  missionDate: string;
  status: 'RECRUITING' | 'IN_PROGRESS' | 'CLOSED';
  createdAt: string;
  updatedAt: string;
}

export interface MissionList
  extends Pick<
    Mission,
    | 'missionId'
    | 'title'
    | 'difficulty'
    | 'wage'
    | 'status'
    | 'missionDate'
    | 'currentParticipants'
    | 'maxParticipants'
  > {
  s3Keys: string[];
  images: { id: number; objectKey: string }[];
  participationId?: number;
}

export interface ParticipationInfo {
  participationId: number;
  memberId: number;
  memberName: string;
  memberProfileImage: string;
}

export interface MissionRaw {
  missionId: number;
  title: string;
  content: string;
  s3Keys: string[];
  images: { id: number; objectKey: string }[];
  difficulty: number;
  maxParticipants: number;
  currentParticipants: number;
  wage: number;
  missionDate: string;
  participations?: ParticipationInfo[];
}

export interface MissionCreateRequest {
  title: string;
  content: string;
  s3Keys: string[];
  images: { id: number; objectKey: string }[];
  difficulty: number;
  maxParticipants: number;
  wage: number;
  missionDate: string;
}

export interface MissionUpdateRequest extends MissionCreateRequest {
  missionId: number;
}

export interface MissionImage {
  missionImageId: number;
  missionId: number;
  imageUrl: string;
}

export interface MissionParticipation {
  participationId: number;
  memberId: number;
  missionId: number;
  status: 'IN_PROGRESS' | 'CHECKING' | 'SUCCESS' | 'FAILED' | 'REJECTED';
}

export interface ParticipationUserInfo {
  participationId: number;
  name: string;
  profileImage: string;
  creditRating: number;
  status: 'IN_PROGRESS' | 'CHECKING' | 'SUCCESS' | 'FAILED' | 'REJECTED';
}

export interface MissionDetail extends Mission {
  missionImages: MissionImage[];
  missionParticipations: MissionParticipation[];
}

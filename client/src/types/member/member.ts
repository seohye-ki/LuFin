export interface Member {
  memberId: number;
  email: string;
  password: string;
  secondaryPassword: string;
  memberRole: 'TEACHER' | 'STUDENT';
  profileImage: string;
  name: string;
  certificationNumber: string;
  createdAt: string;
  updatedAt: string;
  lastLogin: string;
  creditRating: number;
  creditStatus: number;
  creditStatusDescription: string;
  activationStatus: number;
}

export const members: Member[] = [
  {
    memberId: 1,
    email: 'test@test.com',
    password: 'test',
    secondaryPassword: 'test',
    memberRole: 'TEACHER',
    profileImage: 'https://picsum.photos/200/300?random=1',
    name: '김철수',
    certificationNumber: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    lastLogin: '2021-01-01',
    creditRating: 100,
    creditStatus: 1,
    creditStatusDescription: '활동 중',
    activationStatus: 1,
  },
  {
    memberId: 2,
    email: 'test2@test.com',
    password: 'test',
    secondaryPassword: 'test',
    memberRole: 'STUDENT',
    profileImage: 'https://picsum.photos/200/300?random=2',
    name: '이영희',
    certificationNumber: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    lastLogin: '2021-01-01',
    creditRating: 100,
    creditStatus: 1,
    creditStatusDescription: '활동 중',
    activationStatus: 1,
  },
  {
    memberId: 3,
    email: 'test3@test.com',
    password: 'test',
    secondaryPassword: 'test',
    memberRole: 'STUDENT',
    profileImage: 'https://picsum.photos/200/300?random=3',
    name: '박지민',
    certificationNumber: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    lastLogin: '2021-01-01',
    creditRating: 100,
    creditStatus: 1,
    creditStatusDescription: '활동 중',
    activationStatus: 1,
  },
];

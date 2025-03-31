export interface Member {
  member_id: number;
  email: string;
  password: string;
  secondary_password: string;
  member_role: 'TEACHER' | 'STUDENT';
  profile_image: string;
  name: string;
  certification_number: string;
  createdAt: string;
  updatedAt: string;
  last_login: string;
  credit_rating: number;
  credit_status: number;
  credit_status_description: string;
  activation_status: number;
}

export const members: Member[] = [
  {
    member_id: 1,
    email: 'test@test.com',
    password: 'test',
    secondary_password: 'test',
    member_role: 'TEACHER',
    profile_image: 'https://picsum.photos/200/300?random=1',
    name: '김철수',
    certification_number: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    last_login: '2021-01-01',
    credit_rating: 100,
    credit_status: 1,
    credit_status_description: '활동 중',
    activation_status: 1,
  },
  {
    member_id: 2,
    email: 'test2@test.com',
    password: 'test',
    secondary_password: 'test',
    member_role: 'STUDENT',
    profile_image: 'https://picsum.photos/200/300?random=2',
    name: '이영희',
    certification_number: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    last_login: '2021-01-01',
    credit_rating: 100,
    credit_status: 1,
    credit_status_description: '활동 중',
    activation_status: 1,
  },
  {
    member_id: 3,
    email: 'test3@test.com',
    password: 'test',
    secondary_password: 'test',
    member_role: 'STUDENT',
    profile_image: 'https://picsum.photos/200/300?random=3',
    name: '박지민',
    certification_number: '1234567890',
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
    last_login: '2021-01-01',
    credit_rating: 100,
    credit_status: 1,
    credit_status_description: '활동 중',
    activation_status: 1,
  },
];

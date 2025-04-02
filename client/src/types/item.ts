import { dateUtil, DateUtil } from '../libs/utils/date-util';
import { Member } from './member/member';

export interface Item {
  itemId: number;
  classroomId: number;
  name: string;
  price: number;
  quantityAvailable: number;
  quantitySold: number;
  status: boolean;
  expirationDate: DateUtil;
  createdAt: DateUtil;
  updatedAt: DateUtil;
}

export type ItemPreview = Pick<
  Item,
  'itemId' | 'name' | 'price' | 'quantitySold' | 'quantityAvailable' | 'expirationDate' | 'status'
>;

export type StudentPreview = Pick<Member, 'name' | 'member_id' | 'profile_image'>;

export type ItemDetail = ItemPreview & {
  buy: StudentPreview[];
  request: StudentPreview[];
  used: StudentPreview[];
  expired: StudentPreview[];
};

export const initialItemPreviews: ItemPreview[] = [
  {
    itemId: 1,
    name: '급식줄 맨앞에 서기',
    price: 5000,
    quantitySold: 0,
    quantityAvailable: 100,
    status: true,
    expirationDate: dateUtil('2025-05-01T10:00:00'),
  },
  {
    itemId: 2,
    name: '반성문 면제권',
    price: 3000,
    quantitySold: 0,
    quantityAvailable: 50,
    status: true,
    expirationDate: dateUtil('2025-06-15T10:00:00'),
  },
  {
    itemId: 3,
    name: '일기 면제권',
    price: 2000,
    quantitySold: 0,
    quantityAvailable: 200,
    status: true,
    expirationDate: dateUtil('2025-07-20T10:00:00'),
  },
  {
    itemId: 4,
    name: '시험지 공개권',
    price: 10000,
    quantitySold: 0,
    quantityAvailable: 30,
    status: false,
    expirationDate: dateUtil('2025-04-01T10:00:00'),
  },
  {
    itemId: 5,
    name: '수업 시간 중 전화통화권',
    price: 7000,
    quantitySold: 0,
    quantityAvailable: 40,
    status: true,
    expirationDate: dateUtil('2025-08-10T10:00:00'),
  },
  {
    itemId: 6,
    name: '체육시간 대체권',
    price: 15000,
    quantitySold: 0,
    quantityAvailable: 20,
    status: false,
    expirationDate: dateUtil('2025-03-15T10:00:00'),
  },
  {
    itemId: 7,
    name: '화장실 자유 출입권',
    price: 8000,
    quantitySold: 0,
    quantityAvailable: 150,
    status: true,
    expirationDate: dateUtil('2025-09-01T10:00:00'),
  },
];

export const initialItemDetails: ItemDetail[] = [
  {
    itemId: 1,
    name: '급식줄 맨앞에 서기',
    price: 5000,
    quantitySold: 10,
    quantityAvailable: 90,
    status: true,
    expirationDate: dateUtil('2025-05-01T10:00:00'),
    buy: [
      {
        name: '김민수',
        member_id: 1,
        profile_image: 'https://picsum.photos/200/300?random=3',
      },
      {
        name: '이영희',
        member_id: 2,
        profile_image: 'https://picsum.photos/200/300?random=3',
      },
    ],
    request: [
      {
        name: '박지훈',
        member_id: 3,
        profile_image: 'https://picsum.photos/200/300?random=3',
      },
    ],
    used: [
      {
        name: '정수빈',
        member_id: 4,
        profile_image: 'https://picsum.photos/200/300?random=3',
      },
    ],
    expired: [
      {
        name: '최민호',
        member_id: 5,
        profile_image: 'https://picsum.photos/200/300?random=3',
      },
    ],
  },
  {
    itemId: 2,
    name: '반성문 면제권',
    price: 3000,
    quantitySold: 5,
    quantityAvailable: 45,
    status: true,
    expirationDate: dateUtil('2025-06-15T10:00:00'),
    buy: [
      { name: '김지연', member_id: 6, profile_image: 'https://picsum.photos/200/300?random=3' },
    ],
    request: [
      { name: '유준석', member_id: 7, profile_image: 'https://picsum.photos/200/300?random=3' },
      { name: '조수연', member_id: 8, profile_image: 'https://picsum.photos/200/300?random=3' },
    ],
    used: [
      { name: '이서준', member_id: 9, profile_image: 'https://picsum.photos/200/300?random=3' },
    ],
    expired: [
      { name: '한상훈', member_id: 10, profile_image: 'https://picsum.photos/200/300?random=3' },
    ],
  },
  {
    itemId: 3,
    name: '일기 면제권',
    price: 2000,
    quantitySold: 0,
    quantityAvailable: 200,
    status: true,
    expirationDate: dateUtil('2025-07-20T10:00:00'),
    buy: [],
    request: [
      { name: '김하늘', member_id: 11, profile_image: 'https://picsum.photos/200/300?random=3' },
    ],
    used: [],
    expired: [],
  },
  {
    itemId: 4,
    name: '시험지 공개권',
    price: 10000,
    quantitySold: 3,
    quantityAvailable: 27,
    status: false,
    expirationDate: dateUtil('2025-04-01T10:00:00'),
    buy: [{ name: '이찬영', member_id: 12, profile_image: 'path/to/image12.jpg' }],
    request: [],
    used: [],
    expired: [{ name: '박주형', member_id: 13, profile_image: 'path/to/image13.jpg' }],
  },
  {
    itemId: 5,
    name: '수업 시간 중 전화통화권',
    price: 7000,
    quantitySold: 8,
    quantityAvailable: 32,
    status: true,
    expirationDate: dateUtil('2025-08-10T10:00:00'),
    buy: [{ name: '이정훈', member_id: 14, profile_image: 'path/to/image14.jpg' }],
    request: [],
    used: [{ name: '박진수', member_id: 15, profile_image: 'path/to/image15.jpg' }],
    expired: [],
  },
  {
    itemId: 6,
    name: '체육시간 대체권',
    price: 15000,
    quantitySold: 0,
    quantityAvailable: 20,
    status: false,
    expirationDate: dateUtil('2025-03-15T10:00:00'),
    buy: [],
    request: [],
    used: [],
    expired: [{ name: '조현정', member_id: 16, profile_image: 'path/to/image16.jpg' }],
  },
  {
    itemId: 7,
    name: '화장실 자유 출입권',
    price: 8000,
    quantitySold: 0,
    quantityAvailable: 150,
    status: true,
    expirationDate: dateUtil('2025-09-01T10:00:00'),
    buy: [
      { name: '김재훈', member_id: 17, profile_image: 'path/to/image17.jpg' },
      { name: '황수민', member_id: 18, profile_image: 'path/to/image18.jpg' },
    ],
    request: [{ name: '손경호', member_id: 19, profile_image: 'path/to/image19.jpg' }],
    used: [{ name: '강지수', member_id: 20, profile_image: 'path/to/image20.jpg' }],
    expired: [],
  },
];

import Profile from '../../../components/Profile/Profile';
import Lufin from '../../../components/Lufin/Lufin';
import Button from '../../../components/Button/Button';
import Badge from '../../../components/Badge/Badge';
import Alert from '../../../components/Alert/Alert';
import { Icon, IconProps, IconsaxIconName } from '../../../components/Icon/Icon';
import Sidebar from '../../../components/Sidebar/Sidebar';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import DefaultLayout from '../../../components/Layout/DefaultLayout';
import Checkbox from '../../../components/Form/Checkbox';
import TextField from '../../../components/Form/TextField';
import TextArea from '../../../components/Form/TextArea';
import ImageUpload from '../../../components/Form/ImageUpload';
import { useState } from 'react';
import DashBoardCard from '../../../components/Card/Card';
import TableView, { TableColumn, TableRow } from '../../../components/Frame/TableView';
import LoginView from '../../../components/Frame/LoginView';
import Dropdown from '../../../components/Form/Dropdown';
import CodeInput from '../../../components/Form/CodeInput';
import CalendarView from '../../../components/Calendar/Calendar';
import CreditChart from '../../../components/Graph/CreditChart';
import StockChart from '../../../components/Graph/StockChart';
import StockGraph from '../../../components/Graph/StockGraph';
import { dateUtil } from '../../../libs/utils/date-util';

const CommonComponents = () => {
  const navigationIcons: IconsaxIconName[] = [
    'ArrowLeft',
    'ArrowRight',
    'ArrowUp',
    'ArrowDown',
    'ArrowLeft2',
    'ArrowRight2',
    'ArrowUp2',
    'ArrowDown2',
    'ArrowCircleLeft',
    'ArrowCircleRight',
    'ArrowCircleUp',
    'ArrowCircleDown',
    'ArrowCircleLeft2',
    'ArrowCircleRight2',
    'ArrowCircleUp2',
    'ArrowCircleDown2',
    'ArrowSquareLeft',
    'ArrowSquareRight',
    'ArrowSquareUp',
    'ArrowSquareDown',
  ];

  const statusIcons: IconsaxIconName[] = [
    'InfoCircle',
    'Notification',
    'CloseCircle',
    'TickCircle',
    'Alarm',
    'MinusSquare',
    'MinusCirlce',
    'AddCircle',
  ];

  const actionIcons: IconsaxIconName[] = [
    'Edit',
    'Edit2',
    'Trash',
    'Filter',
    'Setting4',
    'SearchNormal1',
    'HambergerMenu',
    'MoreCircle',
    'Add',
    'More',
    'Close',
    'CircleEdit',
    'CircleTrash',
    'CircleAdd',
  ];

  const businessIcons: IconsaxIconName[] = [
    'Shop',
    'ShoppingCart',
    'DollarSquare',
    'ChartSquare',
    'Wallet',
    'Briefcase',
    'TaskSquare',
    'GalleryAdd',
  ];

  const userIcons: IconsaxIconName[] = [
    'ProfileCircle',
    'Profile2User',
    'Personalcard',
    'Home2',
    'Star',
  ];

  const variants: IconProps['variant'][] = [
    'Linear',
    'Outline',
    'Broken',
    'Bold',
    'Bulk',
    'TwoTone',
  ];

  const columns: TableColumn[] = [
    { key: 'mission', label: '미션명' },
    { key: 'wage', label: '급여' },
    { key: 'difficulty', label: '난이도' },
    { key: 'date', label: '수행 날짜' },
    { key: 'participant', label: '수행 인원' },
    { key: 'status', label: '상태' },
    { key: 'request', label: '요청' },
  ];

  const rows: TableRow[] = [
    {
      mission: '안내문 나눠주기',
      wage: <Lufin count={1000} size='s' />,
      difficulty: (
        <div className='flex items-center gap-1'>
          <Icon name='Star' size={24} color='yellow' variant='Bold' />
          <Icon name='Star' size={24} color='yellow' variant='Bold' />
          <Icon name='Star' size={24} color='yellow' variant='Bold' />
        </div>
      ),
      date: '2025-01-01',
      participant: '1/2',
      status: <Badge status='done'>성공</Badge>,
      request: (
        <Button variant='ghost' color='info'>
          리뷰 요청하기
        </Button>
      ),
    },
    {
      mission: '안내문 나눠주기',
      wage: <Lufin count={1000} size='s' />,
      difficulty: <Icon name='Star' size={24} color='yellow' variant='Bold' />,
      date: '2025-01-01',
      participant: '1/2',
      status: <Badge status='ing'>수행 중</Badge>,
      request: (
        <Button variant='solid' color='primary'>
          신청하기
        </Button>
      ),
    },
  ];

  // Checkbox states
  const [checkbox1, setCheckbox1] = useState(false);
  const [checkbox2, setCheckbox2] = useState(true);
  const [checkbox5, setCheckbox5] = useState(false);

  // Image Upload states
  const [singleImage, setSingleImage] = useState<File[]>([]);
  const [multipleImages, setMultipleImages] = useState<File[]>([]);
  const [errorImage, setErrorImage] = useState<File[]>([]);

  // Dropdown states
  const [selectedValue, setSelectedValue] = useState<
    string | number | { type: 'star'; count: 1 | 2 | 3 } | null
  >(null);
  const [selectedStars, setSelectedStars] = useState<{ type: 'star'; count: 1 | 2 | 3 } | null>(
    null,
  );

  // Dropdown items
  const dropdownItems = [
    { label: '옵션 1', value: '1' },
    { label: '옵션 2', value: '2' },
    { label: '옵션 3', value: '3' },
  ];

  const starDropdownItems = [
    { label: '', value: { type: 'star' as const, count: 1 as const } },
    { label: '', value: { type: 'star' as const, count: 2 as const } },
    { label: '', value: { type: 'star' as const, count: 3 as const } },
  ];

  // CodeInput states
  const [code1, setCode1] = useState('');
  const [code2, setCode2] = useState('');
  const [code3, setCode3] = useState('');
  const [code4, setCode4] = useState('');

  return (
    <div className='p-8 bg-white'>
      <h1 className='text-h1 font-regular text-black mb-4'>Common Components</h1>
      <div className='flex flex-col gap-8'>
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Graph</h2>
          <div className='w-full bg-grey-30 p-4 rounded-2xl flex flex-col gap-4'>
            <div className='flex flex-row w-full gap-4'>
              <div className='bg-white p-4 rounded-2xl w-full'>
                <CreditChart creditRating='A+'></CreditChart>
              </div>

              <div className='bg-white p-4 rounded-2xl w-full'>
                <StockChart
                  stocks={[
                    { stock: '삼성', amount: 20000 },
                    { stock: 'LG', amount: 30000 },
                    { stock: '메리츠', amount: 52000 },
                  ]}
                ></StockChart>
              </div>
            </div>

            <div className='bg-white p-4 rounded-2xl w-full'>
              <StockGraph
                stockPriceInfos={[
                  {
                    date: dateUtil('2025-03-27T09:00:00Z'),
                    price: 10250,
                  },
                  {
                    date: dateUtil('2025-03-27T15:00:00Z'),
                    price: 10500,
                  },
                  {
                    date: dateUtil('2025-03-28T09:00:00Z'),
                    price: 10100,
                  },
                  {
                    date: dateUtil('2025-03-28T15:00:00Z'),
                    price: 10350,
                  },
                  {
                    date: dateUtil('2025-03-29T09:00:00Z'),
                    price: 10400,
                  },
                  {
                    date: dateUtil('2025-03-29T15:00:00Z'),
                    price: 10700,
                  },
                  {
                    date: dateUtil('2025-03-30T09:00:00Z'),
                    price: 10800,
                  },
                  {
                    date: dateUtil('2025-03-30T15:00:00Z'),
                    price: 11000,
                  },
                  {
                    date: dateUtil('2025-03-31T09:00:00Z'),
                    price: 10600,
                  },
                  {
                    date: dateUtil('2025-03-31T15:00:00Z'),
                    price: 10950,
                  },
                  {
                    date: dateUtil('2025-04-01T09:00:00Z'),
                    price: 11200,
                  },
                  {
                    date: dateUtil('2025-04-01T15:00:00Z'),
                    price: 11350,
                  },
                ]}
              ></StockGraph>
            </div>
          </div>
        </section>
        {/* Calendar Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Calendar</h2>
          <div className='bg-grey-30 p-4 rounded-2xl'>
            <CalendarView />
          </div>
        </section>
        {/* Alert Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Alert</h2>
          <div className='grid grid-cols-1 md:grid-cols-2 gap-4 bg-grey-30 p-4 rounded-2xl'>
            {/* Info Alert - Row buttons */}
            <Alert
              title='새로운 미션이 등록됐어요'
              description='미션 수행하고 루핀을 모아보세요!'
              status='info'
              buttonDirection='row'
              primaryButton={{
                label: '확인하러 가기',
                onClick: () => alert('확인하러 가기 클릭'),
              }}
              secondaryButton={{
                label: '취소',
                onClick: () => alert('취소 클릭'),
              }}
            />

            {/* Warning Alert - Column buttons */}
            <Alert
              title='지금은 매매할 수 없어요'
              description='매매 가능 시간은 매일 오시장 14시까지입니다.'
              status='warning'
              buttonDirection='column'
              primaryButton={{
                label: '확인했습니다',
                onClick: () => alert('확인 클릭'),
                color: 'primary',
              }}
              secondaryButton={{
                label: '이유 보기',
                onClick: () => alert('이유 보기 클릭'),
              }}
            />

            {/* Danger Alert - Single button */}
            <Alert
              title='네트워크 오류'
              description='빠른 시간 내에 조치하겠습니다.'
              status='danger'
              primaryButton={{
                label: '확인했습니다',
                onClick: () => alert('확인 클릭'),
                color: 'danger',
              }}
            />

            {/* Success Alert - Row buttons */}
            <Alert
              title='삭제가 완료됐어요'
              description='삭제된 데이터는 복구할 수 없어요.'
              status='success'
              buttonDirection='row'
              primaryButton={{
                label: '확인',
                onClick: () => alert('확인 클릭'),
                color: 'primary',
              }}
              secondaryButton={{
                label: '되돌리기',
                onClick: () => alert('되돌리기 클릭'),
              }}
            />
          </div>
        </section>

        {/* Icon Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Icons</h2>
          <div className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
            {/* Navigation Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Navigation Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {navigationIcons.map((name) => (
                  <div
                    key={name}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Status Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Status Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {statusIcons.map((name) => (
                  <div
                    key={name}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Action Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Action Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {actionIcons.map((name) => (
                  <div
                    key={name}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Business Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Business Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {businessIcons.map((name) => (
                  <div
                    key={name}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* User Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>User Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {userIcons.map((name) => (
                  <div
                    key={name}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Icon Variants */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>
                Icon Variants (Using InfoCircle)
              </h3>
              <div className='grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4'>
                {variants.map((variant) => (
                  <div
                    key={variant}
                    className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                  >
                    <Icon name='InfoCircle' size={24} variant={variant} />
                    <span className='text-xs text-center'>{variant}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Semantic Colors with Variants */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Semantic Colors with Variants</h3>
              <div className='grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8'>
                {[
                  { name: 'Info', color: 'info' },
                  { name: 'Warning', color: 'warning' },
                  { name: 'Danger', color: 'danger' },
                  { name: 'Success', color: 'success' },
                ].map(({ name, color }) => (
                  <div key={name} className='flex flex-col gap-4'>
                    <span className='text-sm font-medium'>{name}</span>
                    <div className='grid grid-cols-3 gap-4'>
                      {['Linear', 'Outline', 'Bold'].map((variant) => (
                        <div
                          key={variant}
                          className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'
                        >
                          <Icon
                            name='InfoCircle'
                            size={32}
                            color={color}
                            variant={variant as IconProps['variant']}
                          />
                          <span className='text-xs text-center'>{variant}</span>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        </section>

        {/* Profile Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Profile</h2>
          <div className='flex gap-4 bg-grey-30 p-4 rounded-2xl'>
            <div className='bg-white rounded-lg p-4'>
              <Profile name='이재현' profileImage='https://picsum.photos/200/300?random=1' />
            </div>
            <div className='bg-white rounded-lg p-4'>
              <Profile
                name='이재현'
                profileImage='https://picsum.photos/200/300?random=1'
                variant='row'
              />
            </div>
            <div className='bg-white rounded-lg p-4'>
              <Profile
                name='이재현 선생님'
                profileImage='https://picsum.photos/200/300?random=1'
                variant='row'
                className='border border-purple-30 rounded-lg'
              />
            </div>
          </div>
        </section>

        {/* Card Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Card</h2>
          <div className='grid grid-cols-1 md:grid-cols-2 gap-4 bg-grey-30 p-4 rounded-2xl'>
            {/* Modal Card */}
            <div className='flex flex-col gap-4'>
              <h3 className='text-p1 font-medium text-black mb-2'>모달 카드</h3>
              <DashBoardCard
                titleLeft='제목'
                titleRight={<Icon name='Close' size={24} color='#8A8D8E' />}
                titleSize='l'
                isModal
              >
                <div className='flex flex-col gap-2'>
                  <p className='text-p2 font-medium text-black'>내용</p>
                </div>
              </DashBoardCard>
            </div>
            {/* Dashboard Card */}
            <div className='flex flex-col gap-4'>
              <h3 className='text-p1 font-medium text-black mb-2'>대시보드 카드</h3>
              <DashBoardCard
                titleLeft='제목'
                titleRight={<Badge status='fail'>실패</Badge>}
                titleSize='l'
                isModal={false}
              />
            </div>
          </div>
        </section>

        {/* Badge Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Badge</h2>
          <div className='flex gap-2 items-center flex-wrap bg-grey-30 p-4 rounded-2xl'>
            <Badge status='ready'>모집 중</Badge>
            <Badge status='ing'>수행 중</Badge>
            <Badge status='reject'>반려</Badge>
            <Badge status='review'>검토 중</Badge>
            <Badge status='fail'>실패</Badge>
            <Badge status='done'>성공</Badge>
          </div>
        </section>

        {/* Lufin Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Lufin</h2>
          <div className='flex flex-col gap-12 bg-grey-30 p-4 rounded-2xl'>
            <Lufin count={15200} size='s' />
            <Lufin count={15200} size='m' />
            <Lufin count={15200} size='l' />
          </div>
        </section>

        {/* Button Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Button</h2>
          <div className='flex flex-col gap-8 bg-white p-4 rounded-2xl'>
            <div>
              <h3 className='text-p1 font-medium text-black mb-2'>기본 버튼 (Solid)</h3>
              <div className='flex gap-2 items-center flex-wrap'>
                <Button variant='solid' color='primary'>
                  기본 버튼
                </Button>
                <Button variant='solid' color='danger'>
                  경고 버튼
                </Button>
                <Button variant='solid' color='neutral'>
                  중립 버튼
                </Button>
                <Button variant='solid' color='disabled'>
                  비활성화 버튼
                </Button>
                <Button variant='solid' color='info'>
                  정보 버튼
                </Button>
              </div>
            </div>

            <div>
              <h3 className='text-p1 font-medium text-black mb-2'>테두리 버튼 (Outline)</h3>
              <div className='flex gap-2 items-center flex-wrap'>
                <Button variant='outline' color='primary'>
                  기본 버튼
                </Button>
                <Button variant='outline' color='danger'>
                  경고 버튼
                </Button>
                <Button variant='outline' color='neutral'>
                  중립 버튼
                </Button>
                <Button variant='outline' color='disabled'>
                  비활성화 버튼
                </Button>
                <Button variant='outline' color='info'>
                  정보 버튼
                </Button>
              </div>
            </div>

            <div>
              <h3 className='text-p1 font-medium text-black mb-2'>텍스트 버튼 (Ghost)</h3>
              <div className='flex gap-2 items-center flex-wrap'>
                <Button variant='ghost' color='primary'>
                  기본 버튼
                </Button>
                <Button variant='ghost' color='danger'>
                  경고 버튼
                </Button>
                <Button variant='ghost' color='neutral'>
                  중립 버튼
                </Button>
                <Button variant='ghost' color='disabled'>
                  비활성화 버튼
                </Button>
                <Button variant='ghost' color='info'>
                  정보 버튼
                </Button>
              </div>
            </div>

            <div>
              <h3 className='text-p1 font-medium text-black mb-2'>버튼 크기</h3>
              <div className='flex gap-2 items-center flex-wrap'>
                <Button size='xs'>아주 작게 버튼</Button>
                <Button size='sm'>작게 버튼</Button>
                <Button size='md'>중간 버튼</Button>
                <Button size='lg'>크게 버튼</Button>
                <Button size='xl'>아주 크게 버튼</Button>
              </div>
            </div>

            <div>
              <h3 className='text-p1 font-medium text-black mb-2'>전체 너비</h3>
              <div className='flex flex-col gap-2'>
                <Button size='md' variant='solid' full>
                  Solid 전체 너비 버튼
                </Button>
                <Button size='md' variant='outline' full>
                  Outline 전체 너비 버튼
                </Button>
                <Button size='md' variant='ghost' full>
                  Ghost 전체 너비 버튼
                </Button>
              </div>
            </div>
          </div>
        </section>

        {/* Form Components Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Form Components</h2>
          <div className='flex flex-col gap-8 bg-white p-4 rounded-2xl'>
            {/* ImageUpload */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>ImageUpload</h3>
              <div className='flex flex-col gap-8'>
                {/* Basic ImageUpload */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>기본 이미지 업로드</span>
                  <ImageUpload
                    id='single-image'
                    label='미션 상세 이미지'
                    value={singleImage}
                    onChange={setSingleImage}
                  />
                </div>

                {/* Multiple ImageUpload */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>다중 이미지 업로드</span>
                  <ImageUpload
                    id='multiple-images'
                    label='미션 인증 이미지'
                    description='최대 4장까지 업로드 가능합니다'
                    maxFiles={4}
                    value={multipleImages}
                    onChange={setMultipleImages}
                  />
                </div>

                {/* ImageUpload with Error */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>에러가 있는 이미지 업로드</span>
                  <ImageUpload
                    id='error-image'
                    label='프로필 이미지'
                    error='이미지를 업로드해주세요'
                    value={errorImage}
                    onChange={setErrorImage}
                  />
                </div>

                {/* Disabled ImageUpload */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>비활성화된 이미지 업로드</span>
                  <ImageUpload id='disabled-image' label='프로필 이미지' isDisabled={true} />
                </div>
              </div>
            </div>

            {/* TextField */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>TextField</h3>
              <div className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
                {/* Basic TextField */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>기본 입력 필드</span>
                  <TextField
                    id='email'
                    name='email'
                    type='email'
                    label='이메일'
                    placeholder='you@example.com'
                  />
                </div>

                {/* TextField with Description */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>설명이 있는 입력 필드</span>
                  <TextField
                    id='email-with-desc'
                    name='email'
                    type='email'
                    label='이메일'
                    placeholder='you@example.com'
                    description='스팸 메일을 보내지 않습니다.'
                  />
                </div>

                {/* TextField with Error */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>에러가 있는 입력 필드</span>
                  <TextField
                    id='email-with-error'
                    name='email'
                    type='email'
                    label='이메일'
                    placeholder='you@example.com'
                    description='올바른 이메일 주소를 입력해주세요.'
                    variant='error'
                  />
                </div>

                {/* Disabled TextField */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>비활성화된 입력 필드</span>
                  <TextField
                    id='email-disabled'
                    name='email'
                    type='email'
                    label='이메일'
                    placeholder='you@example.com'
                    isDisabled={true}
                  />
                </div>
              </div>
            </div>

            {/* TextArea */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>TextArea</h3>
              <div className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
                {/* Basic TextArea */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>기본 텍스트 영역</span>
                  <TextArea
                    id='comment'
                    name='comment'
                    label='댓글'
                    placeholder='댓글을 입력해주세요'
                    rows={4}
                  />
                </div>

                {/* TextArea with Description */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>설명이 있는 텍스트 영역</span>
                  <TextArea
                    id='comment-with-desc'
                    name='comment'
                    label='댓글'
                    placeholder='댓글을 입력해주세요'
                    description='최대 500자까지 입력 가능합니다'
                    rows={4}
                  />
                </div>

                {/* TextArea with Error */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>에러가 있는 텍스트 영역</span>
                  <TextArea
                    id='comment-with-error'
                    name='comment'
                    label='댓글'
                    placeholder='댓글을 입력해주세요'
                    description='댓글을 입력해주세요'
                    rows={4}
                    variant='error'
                  />
                </div>

                {/* Disabled TextArea */}
                <div className='flex flex-col gap-2'>
                  <span className='text-xs'>비활성화된 텍스트 영역</span>
                  <TextArea
                    id='comment-disabled'
                    name='comment'
                    label='댓글'
                    placeholder='댓글을 입력해주세요'
                    isDisabled={true}
                    rows={4}
                  />
                </div>
              </div>
            </div>

            {/* Checkbox */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Checkbox</h3>
              <div className='flex items-center gap-8 bg-grey-30 p-4 rounded-2xl'>
                <div className='flex flex-col items-center'>
                  <Checkbox
                    id='checkbox-1'
                    checked={checkbox1}
                    onChange={(e) => {
                      setCheckbox1(e.target.checked);
                    }}
                  />
                  <span className='text-xs mt-1'>{checkbox1 ? '체크됨' : '체크 안됨'}</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox
                    id='checkbox-2'
                    checked={checkbox2}
                    onChange={(e) => {
                      setCheckbox2(e.target.checked);
                    }}
                  />
                  <span className='text-xs mt-1'>{checkbox2 ? '체크됨' : '체크 안됨'}</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox id='checkbox-3' disabled />
                  <span className='text-xs mt-1'>비활성화</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox id='checkbox-4' disabled defaultChecked />
                  <span className='text-xs mt-1'>체크된 비활성화</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox
                    id='checkbox-5'
                    indeterminate={!checkbox5}
                    checked={checkbox5}
                    onChange={(e) => {
                      setCheckbox5(e.target.checked);
                    }}
                  />
                  <span className='text-xs mt-1'>{!checkbox5 ? '중간 상태' : '체크됨'}</span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Sidebar Section */}
        <section className='flex gap-8 bg-grey-30 p-4 rounded-2xl'>
          <div className='flex flex-col gap-4'>
            <h2 className='text-h2 font-medium text-black mb-4'>학생 사이드바</h2>
            <Sidebar userRole='student' />
          </div>
          <div className='flex flex-col gap-4'>
            <h2 className='text-h2 font-medium text-black mb-4'>선생님 사이드바</h2>
            <Sidebar userRole='teacher' />
          </div>
        </section>

        {/* LoginView Section */}
        <section className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
          <h2 className='text-h2 font-medium text-black mb-4'>LoginView</h2>
          <div className='flex gap-4'>
            <LoginView
              title='반가워요! 이름을 적어주세요'
              description='루핀 회원이라면 모든 서비스를 이용할 수 있습니다.'
              content={
                <TextField id='name' name='name' type='text' placeholder='이름을 적어주세요' />
              }
              secondaryButton={{
                text: '다음',
                onClick: () => {},
              }}
            />
            <LoginView
              title='이메일을 적어주세요.'
              description='루핀 회원이라면 모든 서비스를 이용할 수 있습니다.'
              content={
                <TextField id='email' name='email' type='email' placeholder='you@example.com' />
              }
              primaryButton={{
                text: '이전',
                onClick: () => {},
              }}
              secondaryButton={{
                text: '다음',
                onClick: () => {},
              }}
            />
            <LoginView
              title='환영합니다!'
              description='지금부터 슬기로운 루핀 생활을 시작해봐요'
              content={
                <div className='flex flex-col gap-2 mt-4 items-center'>
                  <Button variant='solid' color='primary' size='lg'>
                    시작하기
                  </Button>
                </div>
              }
              backgroundImage
            />
          </div>
        </section>

        {/* TableView Section */}
        <section className='bg-grey-30 p-4 rounded-2xl'>
          <h2 className='text-h2 font-medium text-black mb-4'>TableView</h2>
          <TableView columns={columns} rows={rows} />
        </section>

        {/* Layout Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Layout</h2>
          <div className='flex gap-8 bg-grey-30 p-4 rounded-2xl'>
            <SidebarLayout>
              <h2 className='text-h2 font-medium text-black mb-4'>Sidebar Layout</h2>
              <p className='text-body'>사이드바가 포함된 레이아웃입니다.</p>
            </SidebarLayout>

            <DefaultLayout>
              <h2 className='text-h2 font-medium text-black mb-4'>Default Layout</h2>
              <p className='text-body'>사이드바가 없는 기본 레이아웃입니다.</p>
            </DefaultLayout>
          </div>
        </section>

        {/* Dropdown Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Dropdown</h2>
          <div className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
            {/* Basic Dropdown */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>기본 드롭다운</span>
              <Dropdown
                label='기본 옵션'
                items={dropdownItems}
                value={selectedValue}
                onChange={(value) => setSelectedValue(value as typeof selectedValue)}
              />
            </div>

            {/* Star Rating Dropdown */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>별점 드롭다운</span>
              <Dropdown
                label='별점'
                items={starDropdownItems}
                value={selectedStars}
                onChange={(value) => setSelectedStars(value as typeof selectedStars)}
              />
            </div>

            {/* Disabled Dropdown */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>비활성화된 드롭다운</span>
              <Dropdown label='비활성화' items={dropdownItems} isDisabled={true} />
            </div>
          </div>
        </section>

        {/* CodeInput Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>CodeInput</h2>
          <div className='flex flex-col gap-8 bg-grey-30 p-4 rounded-2xl'>
            {/* Basic CodeInput */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>기본 코드 입력</span>
              <CodeInput value={code1} onChange={setCode1} length={5} />
            </div>

            {/* CodeInput with Different Sizes */}
            <div className='flex flex-col gap-4'>
              <span className='text-xs'>크기 옵션</span>
              <div className='flex flex-col gap-4'>
                <CodeInput value={code2} onChange={setCode2} length={5} size='sm' />
                <CodeInput value={code3} onChange={setCode3} length={5} size='md' />
                <CodeInput value={code4} onChange={setCode4} length={5} size='lg' />
              </div>
            </div>

            {/* CodeInput with Error */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>에러가 있는 코드 입력</span>
              <CodeInput length={5} error='올바른 코드를 입력해주세요' />
            </div>

            {/* Disabled CodeInput */}
            <div className='flex flex-col gap-2'>
              <span className='text-xs'>비활성화된 코드 입력</span>
              <CodeInput length={5} isDisabled={true} />
            </div>
          </div>
        </section>
      </div>
    </div>
  );
};

export default CommonComponents;

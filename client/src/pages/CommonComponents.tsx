import Profile from '../components/Profile/Profile';
import Lufin from '../components/Lufin/Lufin';
import Button from '../components/Button/Button';
import Badge from '../components/Badge/Badge';
import Alert from '../components/Alert/Alert';
import { Icon, IconProps, IconsaxIconName } from '../components/Icon/Icon';
import Sidebar from '../components/Sidebar/Sidebar';
import SidebarLayout from '../components/Layout/SidebarLayout';
import DefaultLayout from '../components/Layout/DefaultLayout';
import Checkbox from '../components/Form/Checkbox';
import { useState } from 'react';

const CommonComponents = () => {
  const navigationIcons: IconsaxIconName[] = [
    'ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown',
    'ArrowLeft2', 'ArrowRight2', 'ArrowUp2', 'ArrowDown2',
    'ArrowCircleLeft', 'ArrowCircleRight', 'ArrowCircleUp', 'ArrowCircleDown',
    'ArrowCircleLeft2', 'ArrowCircleRight2', 'ArrowCircleUp2', 'ArrowCircleDown2',
    'ArrowSquareLeft', 'ArrowSquareRight', 'ArrowSquareUp', 'ArrowSquareDown',
  ];

  const statusIcons: IconsaxIconName[] = [
    'InfoCircle', 'Notification', 'CloseCircle', 'TickCircle',
    'Alarm', 'MinusSquare', 'MinusCirlce', 'AddCircle',
  ];

  const actionIcons: IconsaxIconName[] = [
    'Edit', 'Edit2', 'Trash', 'Filter', 'Setting4',
    'SearchNormal1', 'HambergerMenu', 'MoreCircle',
    'Add', 'More', 'Close', 'CircleEdit', 'CircleTrash', 'CircleAdd',
  ];

  const businessIcons: IconsaxIconName[] = [
    'Shop', 'ShoppingCart', 'DollarSquare', 'ChartSquare',
    'Wallet', 'Briefcase', 'TaskSquare', 'GalleryAdd',
  ];

  const userIcons: IconsaxIconName[] = [
    'ProfileCircle', 'Profile2User', 'Personalcard', 'Home2',
    'Star',
  ];

  const variants: IconProps['variant'][] = ['Linear', 'Outline', 'Broken', 'Bold', 'Bulk', 'TwoTone'];

  // Checkbox states
  const [checkbox1, setCheckbox1] = useState(false);
  const [checkbox2, setCheckbox2] = useState(true);
  const [checkbox5, setCheckbox5] = useState(false);

  return (
    <div className='p-8 min-h-screen bg-broken-white'>
      <h1 className='text-h1 font-regular text-black mb-4'>Common Components</h1>
      <div className='flex flex-col gap-8'>
        {/* Alert Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Alert</h2>
          <div className='grid grid-cols-1 md:grid-cols-2 gap-4 bg-broken-white p-4 rounded-2xl'>
            {/* Info Alert - Row buttons */}
            <Alert
              title="새로운 미션이 등록됐어요"
              description="미션 수행하고 루핀을 모아보세요!"
              status="info"
              buttonDirection="row"
              primaryButton={{
                label: "확인하러 가기",
                onClick: () => alert("확인하러 가기 클릭"),
              }}
              secondaryButton={{
                label: "취소",
                onClick: () => alert("취소 클릭"),
              }}
            />

            {/* Warning Alert - Column buttons */}
            <Alert
              title="지금은 매매할 수 없어요"
              description="매매 가능 시간은 매일 오시장 14시까지입니다."
              status="warning"
              buttonDirection="column"
              primaryButton={{
                label: "확인했습니다",
                onClick: () => alert("확인 클릭"),
                color: "primary"
              }}
              secondaryButton={{
                label: "이유 보기",
                onClick: () => alert("이유 보기 클릭"),
              }}
            />

            {/* Danger Alert - Single button */}
            <Alert
              title="네트워크 오류"
              description="빠른 시간 내에 조치하겠습니다."
              status="danger"
              primaryButton={{
                label: "확인했습니다",
                onClick: () => alert("확인 클릭"),
                color: "danger"
              }}
            />

            {/* Success Alert - Row buttons */}
            <Alert
              title="삭제가 완료됐어요"
              description="삭제된 데이터는 복구할 수 없어요."
              status="success"
              buttonDirection="row"
              primaryButton={{
                label: "확인",
                onClick: () => alert("확인 클릭"),
                color: "primary"
              }}
              secondaryButton={{
                label: "되돌리기",
                onClick: () => alert("되돌리기 클릭"),
              }}
            />
          </div>
        </section>

        {/* Icon Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Icons</h2>
          <div className='flex flex-col gap-8 bg-broken-white p-4 rounded-2xl'>
            {/* Navigation Icons */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Navigation Icons</h3>
              <div className='grid grid-cols-2 md:grid-cols-4 lg:grid-cols-6 gap-4'>
                {navigationIcons.map((name) => (
                  <div key={name} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
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
                  <div key={name} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
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
                  <div key={name} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
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
                  <div key={name} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
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
                  <div key={name} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
                    <Icon name={name} size={24} />
                    <span className='text-xs text-center'>{name}</span>
                  </div>
                ))}
              </div>
            </div>

            {/* Icon Variants */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Icon Variants (Using InfoCircle)</h3>
              <div className='grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4'>
                {variants.map((variant) => (
                  <div key={variant} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
                    <Icon name="InfoCircle" size={24} variant={variant} />
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
                        <div key={variant} className='flex flex-col items-center gap-2 p-4 bg-white rounded-lg'>
                          <Icon
                            name="InfoCircle"
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
          <div className='flex flex-col gap-4'>
            <Profile name='이재현' profileImage='https://picsum.photos/200/300?random=1' />
            <Profile
              name='이재현 선생님'
              profileImage='https://picsum.photos/200/300?random=1'
              variant='certification'
              certificationNumber='5학년 1반'
            />
          </div>
        </section>

        {/* Badge Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Badge</h2>
          <div className='flex gap-2 items-center flex-wrap bg-broken-white p-4 rounded-2xl'>
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
          <div className='flex flex-col gap-12'>
            <Lufin count={15200} size={16} />
            <Lufin count={15200} size={24} />
            <Lufin count={15200} size={28} />
            <Lufin count={15200} size={32} />
            <Lufin count={15200} size={40} />
            <Lufin count={15200} size={52} />
          </div>
        </section>

        {/* Button Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Button</h2>
          <div className='flex flex-col gap-8 bg-broken-white p-4 rounded-2xl'>
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
          <div className='flex flex-col gap-8 bg-broken-white p-4 rounded-2xl'>
            {/* Checkbox */}
            <div>
              <h3 className='text-p1 font-medium text-black mb-4'>Checkbox</h3>
              <div className='flex items-center gap-8'>
                <div className='flex flex-col items-center'>
                  <Checkbox 
                    id="checkbox-1" 
                    checked={checkbox1}
                    onChange={(e, checked) => setCheckbox1(checked)}
                  />
                  <span className='text-xs mt-1'>{checkbox1 ? '체크됨' : '체크 안됨'}</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox 
                    id="checkbox-2" 
                    checked={checkbox2}
                    onChange={(e, checked) => setCheckbox2(checked)}
                  />
                  <span className='text-xs mt-1'>{checkbox2 ? '체크됨' : '체크 안됨'}</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox id="checkbox-3" disabled />
                  <span className='text-xs mt-1'>비활성화</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox id="checkbox-4" disabled defaultChecked />
                  <span className='text-xs mt-1'>체크된 비활성화</span>
                </div>
                <div className='flex flex-col items-center'>
                  <Checkbox 
                    id="checkbox-5" 
                    indeterminate={!checkbox5}
                    checked={checkbox5}
                    onChange={(e, checked) => setCheckbox5(checked)}
                  />
                  <span className='text-xs mt-1'>
                    {!checkbox5 ? '중간 상태' : '체크됨'}
                  </span>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Sidebar Section */}
        <section className='flex gap-8'>
          <div className='flex flex-col gap-4'>
            <h2 className='text-h2 font-medium text-black mb-4'>학생 사이드바</h2>
            <Sidebar userRole='student' />
          </div>
          <div className='flex flex-col gap-4'>
            <h2 className='text-h2 font-medium text-black mb-4'>선생님 사이드바</h2>
            <Sidebar userRole='teacher' />
          </div>
        </section>

        {/* Layout Section */}
        <section>
          <h2 className='text-h2 font-medium text-black mb-4'>Layout</h2>
          <div className='flex gap-8'>
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
      </div>
    </div>
  );
};

export default CommonComponents;

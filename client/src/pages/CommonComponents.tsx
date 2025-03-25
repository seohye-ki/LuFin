import Profile from '../components/Profile/Profile';
import Lufin from '../components/Lufin/Lufin';
import Button from '../components/Button/Button';
import Sidebar from '../components/Sidebar/Sidebar';

const CommonComponents = () => {
  return (
    <div className='p-8 bg-broken-white'>
      <h1 className='text-h1 font-bold text-black mb-4'>Common Components</h1>
      <div className='flex gap-8'>
        <div className='flex flex-col gap-4'>
          <Profile name='이재현' profileImage='https://picsum.photos/200/300?random=1' />
          <Profile
            name='이재현 선생님'
            profileImage='https://picsum.photos/200/300?random=1'
            variant='certification'
            certificationNumber='5학년 1반 12번'
          />
          <div className='flex flex-col gap-12'>
            <Lufin count={15200} size={16} />
            <Lufin count={15200} size={24} />
            <Lufin count={15200} size={28} />
            <Lufin count={15200} size={32} />
            <Lufin count={15200} size={40} />
            <Lufin count={15200} size={52} />
          </div>
        </div>

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
                <Button size='full' variant='solid'>
                  Solid 전체 너비 버튼
                </Button>
                <Button size='full' variant='outline'>
                  Outline 전체 너비 버튼
                </Button>
                <Button size='full' variant='ghost'>
                  Ghost 전체 너비 버튼
                </Button>
              </div>
            </div>
          </div>
        </section>
        <Sidebar userRole='student' />
        <Sidebar userRole='teacher' />
      </div>
    </div>
  );
};

export default CommonComponents;

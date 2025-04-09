import { useEffect, useState } from 'react';
import { Tab } from '@headlessui/react';
import clsx from 'clsx';
import { Container } from './Container';
import dashboard from '../../../../assets/images/mockups/dashboard.png';
import missionStudent from '../../../../assets/images/mockups/mission_student.png';
import loanStudent from '../../../../assets/images/mockups/loan_student.png';
import item from '../../../../assets/images/mockups/item.png';
import investStudent from '../../../../assets/images/mockups/invest_student.png';

const features = [
  {
    title: '대시보드',
    description:
      '루핀의 핵심 정보를 한눈에! 내 루핀 잔액, 투자 현황, 임무 달성률을 실시간으로 확인하세요.',
    image: dashboard,
  },
  {
    title: '미션 수행',
    description:
      '교실에서 실천하는 금융 교육. 매일 새로운 임무를 수행하고 루핀을 획득하며 금융 지식을 쌓아보세요.',
    image: missionStudent,
  },
  {
    title: '대출 신청',
    description:
      '루핀에서 필요한 자금을 대출받으세요. 다양한 대출 상품의 이자율과 조건을 비교하고 선택하세요.',
    image: loanStudent,
  },
  {
    title: '아이템 소비',
    description:
      '루핀에서만 사용할 수 있는 특별한 아이템을 구매하세요. 루핀으로 다양한 혜택을 받아보세요.',
    image: item,
  },
  {
    title: '주식 투자',
    description:
      '루핀의 주식 시장에서 투자 경험을 쌓으세요. 실제와 유사한 환경에서 투자 지식을 키워보세요.',
    image: investStudent,
  },
];

export function PrimaryFeatures() {
  const [tabOrientation, setTabOrientation] = useState<'horizontal' | 'vertical'>('horizontal');

  useEffect(() => {
    const lgMediaQuery = window.matchMedia('(min-width: 1024px)');

    function onMediaQueryChange({ matches }: { matches: boolean }) {
      setTabOrientation(matches ? 'vertical' : 'horizontal');
    }

    onMediaQueryChange(lgMediaQuery);
    lgMediaQuery.addEventListener('change', onMediaQueryChange);

    return () => {
      lgMediaQuery.removeEventListener('change', onMediaQueryChange);
    };
  }, []);

  return (
    <section
      id='features'
      aria-label='주요 기능'
      className='relative overflow-hidden bg-info pt-20 pb-28 sm:py-32'
    >
      <Container className='relative'>
        <div className='max-w-2xl md:mx-auto md:text-center xl:max-w-none'>
          <h2 className='font-pretendard text-h1 font-bold tracking-tight text-white'>
            <span className='block'>루핀</span>
            <span className='mt-2 block font-pretendard text-h2 font-medium text-light-cyan'>
              학생을 위한 맞춤형 금융 교육 플랫폼
            </span>
          </h2>
          <p className='mt-8 font-pretendard text-p1 font-medium text-light-cyan/80 max-w-3xl mx-auto'>
            루핀으로 시작하는 새로운 금융 교육.
            <br className='hidden sm:inline' />
            재정 관리부터 투자 학습까지, 학생들의 금융 생활을 한 곳에서 관리하세요.
          </p>
        </div>

        <Tab.Group
          className='mt-16 grid grid-cols-1 items-center gap-y-2 pt-10 sm:gap-y-6 md:mt-20 lg:grid-cols-12 lg:pt-0'
          vertical={tabOrientation === 'vertical'}
        >
          {({ selectedIndex }) => (
            <>
              <div className='-mx-4 flex overflow-x-auto pb-4 sm:mx-0 sm:overflow-visible sm:pb-0 lg:col-span-5'>
                <Tab.List className='relative z-10 flex gap-x-4 px-4 whitespace-nowrap sm:mx-auto sm:px-0 lg:mx-0 lg:block lg:gap-x-0 lg:gap-y-1 lg:whitespace-normal'>
                  {features.map((feature, featureIndex) => (
                    <div
                      key={feature.title}
                      className={clsx(
                        'group relative rounded-full px-4 py-1 lg:rounded-l-xl lg:rounded-r-none lg:p-6',
                        selectedIndex === featureIndex
                          ? 'bg-white lg:bg-white/10 lg:ring-1 lg:ring-white/10 lg:ring-inset'
                          : 'hover:bg-white/10 lg:hover:bg-white/5',
                      )}
                    >
                      <h3>
                        <Tab
                          className={clsx(
                            'font-pretendard text-h3 font-medium outline-none',
                            selectedIndex === featureIndex
                              ? 'text-info lg:text-white'
                              : 'text-light-cyan hover:text-white lg:text-white',
                          )}
                        >
                          <span className='absolute inset-0 rounded-full lg:rounded-l-xl lg:rounded-r-none' />
                          {feature.title}
                        </Tab>
                      </h3>
                      <p
                        className={clsx(
                          'mt-2 hidden font-pretendard text-p2 lg:block',
                          selectedIndex === featureIndex
                            ? 'text-white'
                            : 'text-light-cyan group-hover:text-white',
                        )}
                      >
                        {feature.description}
                      </p>
                    </div>
                  ))}
                </Tab.List>
              </div>
              <Tab.Panels className='lg:col-span-7'>
                {features.map((feature) => (
                  <Tab.Panel key={feature.title} unmount={false}>
                    <div className='relative sm:px-6 lg:hidden'>
                      <div className='absolute -inset-x-4 top-[-6.5rem] bottom-[-4.25rem] bg-white/10 ring-1 ring-white/10 ring-inset sm:inset-x-0 sm:rounded-t-xl' />
                      <p className='relative mx-auto max-w-2xl font-pretendard text-p1 text-white sm:text-center'>
                        {feature.description}
                      </p>
                    </div>
                    <div className='mt-10 w-[45rem] overflow-hidden rounded-xl bg-broken-white shadow-xl shadow-info/20 sm:w-auto lg:mt-0 lg:w-[67.8125rem]'>
                      <img
                        className='w-full'
                        src={feature.image}
                        alt=''
                        sizes='(min-width: 1024px) 67.8125rem, (min-width: 640px) 100vw, 45rem'
                      />
                    </div>
                  </Tab.Panel>
                ))}
              </Tab.Panels>
            </>
          )}
        </Tab.Group>
      </Container>
    </section>
  );
}

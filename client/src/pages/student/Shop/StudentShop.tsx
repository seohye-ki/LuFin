import Card from '../../../components/Card/Card';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { dateUtil } from '../../../libs/utils/date-util';
import Awning from './components/Awning';
import Item from './components/Item';
import { ReactNode, useState } from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import useAlertStore from '../../../libs/store/alertStore'; // zustand store
import Alert from '../../../components/Alert/Alert';

const itemsData = [
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권1', price: 1000, quantity: 1 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권2', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권3', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권4', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권5', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권6', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권7', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권8', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권9', price: 1000 },
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권10', price: 1000 },
];

const StudentShop = () => {
  const [items] = useState(itemsData);
  const { showAlert, hideAlert } = useAlertStore((state) => state);

  const settings = {
    speed: 1000,
    slidesToShow: 4,
    slidesToScroll: 4,
    dots: true,
    arrows: false,
    autoSlide: true,
  };

  const handleItemClick = (item) => {
    showAlert(
      '아래 아이템을 구매하시겠습니까?',
      <Item expired={item.expired} name={item.name} price={item.price} quantity={item.quantity} />,
      '구매한 아이템은 환불할 수 없어요.',
      'success',
      {
        label: '구매할게요.',
        onClick: () => {
          hideAlert();
        },
      },
      {
        label: '다시 올게요.',
        onClick: () => {
          hideAlert();
        },
      },
    );
  };

  return (
    <SidebarLayout>
      <div className='w-full h-full flex flex-col gap-4'>
        <Card titleLeft='내 아이템' className='w-full h-fit pb-7'>
          <Slider {...settings}>
            {items.map((item, index) => (
              <div key={index} onClick={() => handleItemClick(item)}>
                <Item expired={item.expired} name={item.name} price={item.price} />
              </div>
            ))}
          </Slider>
        </Card>

        <Card className='w-full h-full bg-yellow-30 overflow-hidden'>
          <Awning />
          <div className='w-full h-fit flex flex-row flex-wrap gap-4 justify-start items-start overflow-y-auto'>
            {items.map((item, index) => (
              <div
                key={index}
                className='w-[calc(33.33%-0.67rem)] h-fit'
                onClick={() => handleItemClick(item)}
              >
                <Item
                  expired={item.expired}
                  name={item.name}
                  price={item.price}
                  quantity={item.quantity}
                />
              </div>
            ))}
          </div>
        </Card>
      </div>
    </SidebarLayout>
  );
};

export default StudentShop;

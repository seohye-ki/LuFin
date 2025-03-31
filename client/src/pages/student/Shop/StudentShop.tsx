import Card from '../../../components/Card/Card';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { dateUtil } from '../../../libs/utils/date-util';
import Awning from './components/Awning';
import Item from './components/Item';
import { useState, useEffect } from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

const itemsData = [
  { expired: dateUtil('2025-03-28T10:21:12'), name: '안마권1', price: 1000 },
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
  const [items, setItems] = useState(itemsData);

  const settings = {
    speed: 1000,
    slidesToShow: 3,
    slidesToScroll: 3,
    dots: true,
    arrows: false,
    autoSlide: true,
  };

  return (
    <SidebarLayout>
      <div className='w-full h-full flex flex-col gap-4'>
        <Card titleLeft='내 아이템' className='w-full h-fit pb-7'>
          <Slider {...settings}>
            {items.map((item, index) => (
              <div key={index}>
                <Item expired={item.expired} name={item.name} price={item.price} />
              </div>
            ))}
          </Slider>
        </Card>
        <Card className='w-full h-full bg-yellow-30 overflow-hidden'>
          <Awning />
          <div className='w-full h-fit flex flex-row flex-wrap gap-4 justify-start items-start overflow-y-auto'>
            {items.map((item, index) => (
              <div key={index} className='w-[calc(33.33%-0.67rem)] h-fit'>
                <Item expired={item.expired} name={item.name} price={item.price} />
              </div>
            ))}
          </div>
        </Card>
      </div>
    </SidebarLayout>
  );
};

export default StudentShop;

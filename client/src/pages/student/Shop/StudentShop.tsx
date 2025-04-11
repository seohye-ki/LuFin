import Card from '../../../components/Card/Card';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import Awning from './components/Awning';
import Item from './components/Item';
import { useEffect, useState } from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import useAlertStore from '../../../libs/store/alertStore'; // zustand store
import {
  buyItem,
  getMyItemList,
  getSalesItemList,
  requestUseItem,
} from '../../../libs/services/shop/shop.service';
import { ItemDTO } from '../../../types/shop/item';
import useAuthStore from '../../../libs/store/authStore';

const StudentShop = () => {
  const [myItemList, setMyItemList] = useState<ItemDTO[]>([]);
  const [salesItemList, setSalesItemList] = useState<ItemDTO[]>([]);
  const { showAlert, hideAlert } = useAlertStore((state) => state);

  useEffect(() => {
    const fetchMyItemList = async () => {
      const res = await getMyItemList();
      setMyItemList(res);
    };

    const fetchSalesItemList = async () => {
      const res = await getSalesItemList();
      setSalesItemList(res);
    };

    fetchMyItemList();
    fetchSalesItemList();
  }, []);

  const settings = {
    speed: 1000,
    slidesToShow: 4,
    slidesToScroll: 4,
    dots: true,
    arrows: false,
    autoSlide: true,
    infinite: false,
  };
  const handleMyItemClick = (item: ItemDTO) => {
    showAlert(
      '아래 아이템을 사용할까요?',
      <Item expired={item.expirationDate} name={item.itemName} />,
      `선생님이 승인해주셔야 사용이 완료돼요.`,
      'success',
      {
        label: '사용할게요.',
        onClick: async () => {
          const res: boolean = await requestUseItem(item.purchaseId);

          if (res) {
            setMyItemList(await getMyItemList());

            showAlert(
              '사용 요청을 보냈어요.',
              null,
              '선생님이 승인해주셔야 사용이 완료돼요.',
              'success',
              {
                label: '확인',
                onClick: () => hideAlert(),
              },
            );
          }
        },
      },
      {
        label: '다음에 할게요.',
        onClick: () => {
          hideAlert();
        },
      },
    );
  };

  const handleSalesItemClick = (item: ItemDTO) => {
    showAlert(
      '아래 아이템을 구매하시겠습니까?',
      <Item
        expired={item.expirationDate}
        name={item.itemName}
        price={item.price}
        quantity={item.quantityAvailable - item.quantitySold}
      />,
      '구매한 아이템은 환불할 수 없어요.',
      'success',
      {
        label: '구매할게요.',
        onClick: async () => {
          const res = await buyItem(item.itemId);

          if (res) {
            setMyItemList(await getMyItemList());
            setSalesItemList(await getSalesItemList());
            const { totalAsset } = useAuthStore.getState();
            useAuthStore.setState({ totalAsset: totalAsset - item.price });

            showAlert('아이템을 구매 했어요.', null, '나의 아이템에서 사용해보세요.', 'success', {
              label: '확인',
              onClick: () => hideAlert(),
            });
          }
        },
      },
      {
        label: '다음에 할게요.',
        onClick: () => {
          hideAlert();
        },
      },
    );
  };

  return (
    <SidebarLayout>
      <div className='w-full h-full flex flex-col gap-4'>
        <div className='w-full flex justify-start'>
          <Card titleLeft='내 아이템' className='w-full h-fit'>
            <Slider {...settings}>
              {myItemList.map((item) => {
                return (
                  <div key={item.itemId} onClick={() => handleMyItemClick(item)}>
                    <Item expired={item.expirationDate} name={item.itemName} />
                  </div>
                );
              })}
            </Slider>
          </Card>
        </div>

        <Card className='w-full h-full bg-yellow-30 overflow-hidden'>
          <Awning />
          <div className='w-full h-fit flex flex-row flex-wrap gap-4 justify-start items-start overflow-y-auto'>
            {salesItemList.map((item) => (
              <div
                key={item.itemId}
                className='w-[calc(33.33%-0.67rem)] h-fit'
                onClick={() => handleSalesItemClick(item)}
              >
                <Item
                  expired={item.expirationDate}
                  name={item.itemName}
                  price={item.price}
                  quantity={item.quantityAvailable - item.quantitySold}
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

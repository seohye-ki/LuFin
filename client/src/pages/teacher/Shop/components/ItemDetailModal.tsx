import React, { useEffect, useState } from 'react';
import Card from '../../../../components/Card/Card';
import Lufin from '../../../../components/Lufin/Lufin';
import Profile from '../../../../components/Profile/Profile';

import Button from '../../../../components/Button/Button';
import { ItemDTO, ItemMemberDTO } from '../../../../types/shop/item';
import { getBoughtMemberList } from '../../../../libs/services/shop/shop.service';
import { dateUtil } from '../../../../libs/utils/date-util';

const ItemDetailModal: React.FC<{ item: ItemDTO; closeModal: () => void }> = ({
  item,
  closeModal,
}) => {
  const [categorizedMemberList, setCategorizedMemberList] = useState<{
    buy: ItemMemberDTO[];
    request: ItemMemberDTO[];
    used: ItemMemberDTO[];
    expired: ItemMemberDTO[];
  }>({
    buy: [],
    request: [],
    used: [],
    expired: [],
  });

  useEffect(() => {
    const fetchBoughtMemberList = async () => {
      const res = await getBoughtMemberList(item.itemId);

      const buy = res.filter((m) => m.status === 'BUY');
      const request = res.filter((m) => m.status === 'PENDING');
      const used = res.filter((m) => m.status === 'USED');
      const expired = res.filter((m) => m.status === 'EXPIRED');

      setCategorizedMemberList({ buy, request, used, expired });
    };

    fetchBoughtMemberList();
  }, [item.itemId]);

  return (
    item && (
      <div
        className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
        onClick={closeModal}
      >
        <div onClick={(e) => e.stopPropagation()}>
          <Card className='w-104 h-fit' titleLeft={item.itemName}>
            <div className='flex flex-col gap-6'>
              <ItemDetailSection label='가격'>
                <Lufin size='s' count={item.price} />
              </ItemDetailSection>

              <ItemDetailSection label='기한'>
                <p className='text-p1'>{dateUtil(item.expirationDate).formattedDate}</p>
              </ItemDetailSection>

              <ItemDetailSection label='보유중'>
                <ProfileList
                  itemMembers={categorizedMemberList.buy}
                  message='사용중인 학생이 없습니다.'
                />
              </ItemDetailSection>

              <ItemDetailSection label='사용요청'>
                <ProfileList
                  itemMembers={categorizedMemberList.request}
                  message='사용요청한 학생이 없습니다.'
                />
              </ItemDetailSection>

              <ItemDetailSection label='사용완료'>
                <ProfileList
                  itemMembers={categorizedMemberList.used}
                  message='사용완료한 학생이 없습니다.'
                />
              </ItemDetailSection>

              <ItemDetailSection label='기간만료'>
                <ProfileList
                  itemMembers={categorizedMemberList.expired}
                  message='기간 만료한 학생이 없습니다.'
                />
              </ItemDetailSection>
            </div>
            <Button onClick={closeModal}>확인</Button>
          </Card>
        </div>
      </div>
    )
  );
};

const ItemDetailSection: React.FC<{ label: string; children: React.ReactNode }> = ({
  label,
  children,
}) => {
  return (
    <div className='flex flex-col gap-2'>
      <p className='text-p2 font-semibold text-grey'>{label}</p>
      {children}
    </div>
  );
};

const ProfileList: React.FC<{ itemMembers: ItemMemberDTO[]; message: string }> = ({
  itemMembers,
  message,
}) => {
  if (itemMembers.length === 0) {
    return <p>{message}</p>;
  }

  return (
    <div className='flex flex-row gap-4'>
      {itemMembers.map((itemMember) => (
        <Profile
          key={itemMember.memberId}
          name={itemMember.memberName}
          profileImage={itemMember.memberProfileImage}
          variant='column'
        />
      ))}
    </div>
  );
};

export default ItemDetailModal;

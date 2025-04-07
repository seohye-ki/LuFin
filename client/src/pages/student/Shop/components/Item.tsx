import Lufin from '../../../../components/Lufin/Lufin';
import { dateUtil } from '../../../../libs/utils/date-util';

export interface ItemProps {
  expired: string;
  name: string;
  price?: number;
  quantity?: number;
}

const Item: React.FC<ItemProps> = ({ expired, name, price, quantity }) => {
  return (
    <div className='w-full h-fit bg-broken-white p-4 gap-2 rounded-2xl flex flex-col flex-shrink-0 items-start'>
      <p className='text-c1 font-regular text-grey'>{dateUtil(expired).remainingDays}일 남음</p>
      <p className='text-h3 font-bold text-black'>{name}</p>
      <div className='w-full h-fit flex flex-row justify-between items-center'>
        {price && <Lufin size='s' count={price} />}
        {quantity && <p className='text-c1 font-regular text-grey'>{quantity}개 남음</p>}
      </div>
    </div>
  );
};

export default Item;

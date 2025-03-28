import Lufin from '../../../../components/Lufin/Lufin';
import { DateUtil } from '../../../../libs/utils/date-util';

interface ItemProps {
  expired: DateUtil;
  name: string;
  price: number;
  quantity?: number;
}

const Item: React.FC<ItemProps> = ({ expired, name, price, quantity }) => {
  return (
    <div className='w-full h-fit bg-broken-white p-4 gap-2 rounded-2xl flex flex-col flex-shrink-0'>
      <p className='text-c1 font-regular text-grey'>{expired.day}일 남음</p>
      <p className='text-h3 font-bold text-black'>{name}</p>
      <Lufin size={16} count={price}></Lufin>
    </div>
  );
};

export default Item;

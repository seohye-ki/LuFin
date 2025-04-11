import { Link } from 'react-router-dom';
import DashboardCard from './DashboardCard';
import DashboardPieChart from './DashboardPieChart';
import EmptyStudent from '../../../../assets/images/empty_student.png';
import { paths } from '../../../../routes/paths';

type AssetCardProps = {
  currency?: string;
  assets: {
    cash: number;
    stock: number;
    loan: number;
  };
  totalAsset: number;
};

const AssetCard = ({ currency = '루핀', assets, totalAsset }: AssetCardProps) => {
  const assetData = [
    { stock: '현금', amount: assets.cash + assets.loan },
    { stock: '주식', amount: assets.stock },
  ].filter((item) => item.amount !== 0);

  const renderChartOrImage = () => {
    if (totalAsset === 0) {
      return (
        <div className='flex justify-center items-center mb-4 h-[140px] w-full'>
          <img src={EmptyStudent} alt='자산 없음' className='h-full object-contain' />
        </div>
      );
    }

    return (
      <div className='relative mb-4 h-[140px] w-full'>
        <DashboardPieChart
          stocks={assetData.map((item) => ({
            stock: item.stock,
            amount: Math.abs(item.amount),
          }))}
        />
      </div>
    );
  };

  const renderAssetText = () => {
    if (totalAsset === 0) {
      return (
        <div className='text-center'>
          <div className='text-p2 text-grey mb-0.5'>자산이 없어요.</div>
          <Link to={paths.MISSION} className='text-c1 text-info'>
            미션을 수행하고 돈을 벌어볼까요?
          </Link>
        </div>
      );
    }

    return (
      <div className='text-p2 text-grey text-center flex items-center gap-1'>
        총 자산은
        <div className='inline-flex items-center gap-1'>
          <span className='text-p2 text-info font-bold'>{totalAsset.toLocaleString()}</span>
          <span>{currency}</span>
        </div>
        이에요.
      </div>
    );
  };

  return (
    <DashboardCard titleLeft='자산' className='flex-1 h-full'>
      <div className='h-full flex items-center justify-center'>
        <div className='flex flex-col items-center'>
          {renderChartOrImage()}
          <div className='bg-broken-white px-6 py-1.5 rounded-full'>{renderAssetText()}</div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default AssetCard;

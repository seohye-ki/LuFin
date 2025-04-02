import DashboardCard from './DashboardCard';
import DashboardPieChart from './DashboardPieChart';

type AssetCardProps = {
  currency?: string;
};

const ASSET_DATA = [
  { stock: '현금', amount: 23000 },
  { stock: '적금', amount: 50000 },
  { stock: '주식', amount: 15000 },
  { stock: '대출', amount: -10000 },
];

const AssetCard = ({ currency = '루핀' }: AssetCardProps) => {
  const totalAsset = ASSET_DATA.reduce((sum, item) => sum + item.amount, 0);

  return (
    <DashboardCard titleLeft='자산' className='flex-1 h-full'>
      <div className='h-full flex items-center justify-center'>
        {/* Main content */}
        <div className='flex flex-col items-center'>
          <div className='relative mb-4 h-[140px] w-full'>
            <DashboardPieChart
              stocks={ASSET_DATA.map((item) => ({
                stock: item.stock,
                amount: Math.abs(item.amount),
              }))}
            />
          </div>

          {/* Asset info */}
          <div className='bg-broken-white px-6 py-1.5 rounded-full'>
            <div className='text-p2 text-grey text-center flex items-center gap-1'>
              총 자산은
              <div className='inline-flex items-center gap-1'>
                <span className='text-p2 text-info font-bold'>{totalAsset.toLocaleString()}</span>
                <span>{currency}</span>
              </div>
              이에요.
            </div>
          </div>
        </div>
      </div>
    </DashboardCard>
  );
};

export default AssetCard;

import DashboardCard from './DashboardCard';
import DashboardPieChart from './DashboardPieChart';

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
  // Convert assets to the format required by DashboardPieChart
  const assetData = [
    { stock: '현금', amount: assets.cash },
    { stock: '주식', amount: assets.stock },
    { stock: '대출', amount: Math.abs(assets.loan) * -1 }, // Make loan negative
  ].filter(item => item.amount !== 0); // Filter out zero values

  return (
    <DashboardCard titleLeft='자산' className='flex-1 h-full'>
      <div className='h-full flex items-center justify-center'>
        {/* Main content */}
        <div className='flex flex-col items-center'>
          <div className='relative mb-4 h-[140px] w-full'>
            <DashboardPieChart
              stocks={assetData.map((item) => ({
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

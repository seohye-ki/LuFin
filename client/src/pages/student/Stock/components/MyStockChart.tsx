import MyStockChartCard from './MyStockChartCard';
import MyStockListCard from './MyStockListCard';

const MyStockChart = () => {
  return (
    <div className='flex basis-45/100 min-h-0 gap-3'>
      {/* 차트 영역 */}
      <div className='flex flex-col justify-center bg-white rounded-xl w-[50%]'>
        <MyStockChartCard />
      </div>
      {/* 종목별 상세 정보 */}
      <div className='bg-white rounded-xl w-[50%] min-w-[260px] min-h-0'>
        <MyStockListCard />
      </div>
    </div>
  );
};

export default MyStockChart;

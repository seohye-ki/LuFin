import React from 'react';
import { Doughnut } from 'react-chartjs-2';
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  CategoryScale,
  LinearScale,
  ChartData,
  ChartOptions,
} from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale);

interface stockInfo {
  stock: string;
  amount: number;
}

interface stockChartProps {
  stocks: stockInfo[];
}

const StockChart: React.FC<stockChartProps> = ({ stocks }) => {
  const data: ChartData = {
    labels: stocks.map((stock) => stock.stock),
    datasets: [
      {
        data: stocks.map((stock) => stock.amount),
        backgroundColor: ['#00997E', '#FF414B', '#4ABDE8', '#FFAE41', '#8785FF'],
        borderRadius: 4,
      },
    ],
  };

  const options: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    cutout: '60%',
    plugins: {
      legend: {
        position: 'right',
        labels: {
          usePointStyle: true,
          color: '#242424',
          font: {
            family: 'Pretendard',
            size: 10,
            weight: 400,
          },
        },
      },
    },
  };

  return (
    <div className='w-screen h-screen bg-broken-white flex justify-center items-center'>
      <div className='bg-white w-[388px] h-[234px] rounded-2xl flex flex-col items-center px-16 py-4 gap-4'>
        <div className='w-full h-full'>
          <Doughnut data={data} options={options} />
        </div>
        <p className='text-c2 font-light text-grey'>
          {/* TODO: 전역 상태에서 이름 가져오기 */}님의 보유 주식은 총
          <span className='font-semibold text-c1 text-black'>
            {' ' + stocks.reduce((sum, stock) => sum + stock.amount, 0)}
          </span>
          루핀 이에요.
        </p>
      </div>
    </div>
  );
};

export default StockChart;

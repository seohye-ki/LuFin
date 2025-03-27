import React from 'react';
import { Doughnut } from 'react-ChartJS-2';
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

interface StockInfo {
  stock: string;
  amount: number;
}

interface stockChartProps {
  stocks: StockInfo[];
}

const StockChart: React.FC<stockChartProps> = ({ stocks }) => {
  const data: ChartData = {
    labels: stocks.map((stock) => stock.stock),
    datasets: [
      {
        data: stocks.map((stock) => stock.amount),
        backgroundColor: ['#C3EBFA', '#CFCEFF', '#FAE27C', '#FFDBF7', '#8785FF'],
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
            size: 12,
            weight: 500,
          },
        },
      },
    },
  };

  return (
    <div className='w-full h-full px-48'>
      <Doughnut data={data} options={options} />
    </div>
  );
};

export default StockChart;

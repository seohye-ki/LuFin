import React from 'react';
import {
  ArcElement,
  CategoryScale,
  Chart,
  ChartData,
  Legend,
  LinearScale,
  Title,
  Tooltip,
  PointElement,
  LineElement,
  ChartOptions,
  Filler,
  Plugin,
} from 'chart.js';
import { Line } from 'react-chartjs-2';
import sunIcon from '../../assets/svgs/sun.svg';
import moonIcon from '/src/assets/svgs/moon.svg';
import { DateUtil } from '../../libs/utils/date-util';

Chart.register(
  Title,
  Tooltip,
  Legend,
  Filler,
  ArcElement,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
);

interface StockPriceInfo {
  date: DateUtil;
  price: number;
}

interface StockGraphProps {
  stockPriceInfos: StockPriceInfo[];
}

const StockGraph: React.FC<StockGraphProps> = ({ stockPriceInfos }) => {
  const data: ChartData = {
    labels: stockPriceInfos.map((stockPriceInfo) => {
      return stockPriceInfo.date.dayOfWeek;
    }),
    datasets: [
      {
        label: '주가',
        data: stockPriceInfos.map((stockPriceInfo) => {
          return stockPriceInfo.price;
        }),
        fill: true,
        borderColor: '#FAE27C', // 선 색상
        backgroundColor: 'rgba(0, 0, 0, 0)',
        borderWidth: 1.2,
        tension: 0.2, // 선의 부드러움
        pointRadius: 0,
        hoverRadius: 5,
        hoverBorderWidth: 1.5,
        hoverBackgroundColor: '#FAE27C',
        hoverBorderColor: '#FFFFFF',
      },
    ],
  };

  const options: ChartOptions = {
    responsive: true,
    clip: false,
    maintainAspectRatio: false,
    interaction: {
      mode: 'index',
      intersect: false,
    },
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        titleAlign: 'center',
        bodyAlign: 'center',
        displayColors: false,
        enabled: true,
        borderColor: 'rgba(167,169,170, 0.2)',
        borderWidth: 0.5,
        backgroundColor: 'rgba(255, 255, 255, 0.6)',
        titleColor: '#242424',
        bodyColor: '#8A8D8E',
        titleFont: {
          family: 'Pretendard',
          weight: 500,
          size: 9,
        },
        bodyFont: {
          family: 'Pretendard',
          weight: 400,
          size: 8,
        },
        callbacks: {
          title: (context) => {
            const value = context[0].raw as number;
            return `${value.toLocaleString()} 루핀`;
          },
          label: (context) => {
            const date = stockPriceInfos[context.dataIndex].date;
            return `${date.formattedDate}`;
          },
        },
      },
    },

    scales: {
      x: {
        border: {
          display: false,
        },
        grid: {
          color: 'transparent',
        },
        ticks: {
          color: '#A7A9AA',
          font: {
            family: 'Pretendard',
            weight: 600,
            size: 8,
          },
        },
      },
      y: {
        border: {
          display: false,
          dash: [2, 2],
        },
        grid: {
          color: 'rgba(167,169,170, 0.2)',
          tickColor: 'transparent',
          lineWidth: 0.7,
        },
        ticks: {
          color: '#A7A9AA',
          font: {
            family: 'Pretendard',
            weight: 600,
            size: 8,
          },

          maxTicksLimit: 6,
        },
      },
    },
  };

  const plugins: Plugin[] = [
    {
      id: 'gradientBgPlugin',
      beforeDraw: (chart: Chart) => {
        const ctx = chart.ctx;
        const chartArea = chart.chartArea;

        const gradient = ctx.createLinearGradient(0, chartArea.top, 0, chartArea.bottom);
        gradient.addColorStop(0, 'rgba(250, 226, 124, 0.4)');
        gradient.addColorStop(1, 'rgba(250, 226, 124, 0)');

        chart.data.datasets[0].backgroundColor = gradient;

        chart.update();
      },
    },
    {
      id: 'drawIcons',
      afterDatasetsDraw: (chart) => {
        const {
          ctx,
          data,
          chartArea: { bottom },
          scales: { x },
        } = chart;

        ctx.save();
        data.labels?.forEach((label, index) => {
          const image = new Image();
          const date = stockPriceInfos[index].date;
          image.src = date.hour > 12 ? moonIcon : sunIcon;

          const sunIconWidth = 8;
          const sunIconHeight = 8;
          const moonIconWidth = 6;
          const moonIconHeight = 6;

          const iconWidth = date.hour >= 12 ? moonIconWidth : sunIconWidth;
          const iconHeight = date.hour >= 12 ? moonIconHeight : sunIconHeight;

          const iconX = x.getPixelForValue(index) - iconWidth / 2;
          const iconY = bottom + 3;

          ctx.drawImage(image, iconX, iconY, iconWidth, iconHeight);
        });
      },
    },
    {
      id: 'verticalHoverLine',
      beforeDatasetDraw: (chart) => {
        const {
          ctx,
          chartArea: { top, bottom },
        } = chart;

        ctx.save();

        chart.getDatasetMeta(0).data.forEach((dataPoint) => {
          if (dataPoint.active) {
            ctx.beginPath();
            ctx.strokeStyle = 'rgba(250, 226, 124, 1)';
            ctx.lineWidth = 2;
            ctx.moveTo(dataPoint.x, top);
            ctx.lineTo(dataPoint.x, bottom);
            ctx.stroke();
          }
        });
      },
    },
  ];

  return (
    <div className='w-full h-full'>
      <Line data={data} options={options} plugins={plugins} />
    </div>
  );
};

export default StockGraph;

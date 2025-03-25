import React, { useEffect, useRef } from 'react';
import { Doughnut } from 'react-ChartJS-2';
import {
  Chart,
  Title,
  Tooltip,
  Legend,
  ArcElement,
  CategoryScale,
  LinearScale,
  plugins,
  ChartOptions,
  ChartData,
  Plugin,
} from 'chart.js';

Chart.register(Title, Tooltip, Legend, ArcElement, CategoryScale, LinearScale);

const ratingInfo: { [key: string]: { value: number; text: string } } = {
  'F-': { value: 0, text: '신용 불량자에요.' },
  F: { value: 1, text: '매우 위험해요.' },
  'F+': { value: 2, text: '위험해요.' },
  'D-': { value: 3, text: '주의 해주세요.' },
  D: { value: 4, text: '주의 해주세요.' },
  'D+': { value: 5, text: '주의 해주세요.' },
  'C-': { value: 6, text: '보통이에요.' },
  C: { value: 7, text: '보통이에요.' },
  'C+': { value: 8, text: '보통이에요.' },
  'B-': { value: 9, text: '잘하고 있어요.' },
  B: { value: 10, text: '잘하고 있어요.' },
  'B+': { value: 11, text: '잘하고 있어요.' },
  'A-': { value: 12, text: '아주 좋아요.' },
  A: { value: 13, text: '아주 좋아요.' },
  'A+': { value: 14, text: '최고에요.' },
};

const HalfDoughnutChart: React.FC<{ creditRating: string }> = ({ creditRating }) => {
  const chartRef = useRef<void>(null);
  const { value, text } = ratingInfo[creditRating] || { value: 0, text: '알 수 없음' };

  useEffect(() => {
    if (chartRef.current) {
      chartRef.current.update();
    }
  }, [creditRating]);

  const data: ChartData = {
    datasets: [
      {
        label: '주가',
        data: [value, Object.keys(ratingInfo).length - value],
        backgroundColor: ['#CFCEFF', '#FAE27C'],
        borderRadius: 4,
        borderWidth: 0,
      },
    ],
  };

  const options: ChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    rotation: 270,
    circumference: 180,
    cutout: '60%',
    layout: {
      padding: 0,
    },
    plugins: {
      tooltip: {
        enabled: false,
      },
      legend: {
        display: false,
      },
    },
  };

  const centerTextPlugin: Plugin = {
    id: 'centerTextPlugin',
    beforeDraw: (chart: Chart) => {
      const ctx = chart.ctx;
      const chartWidth = chart.width;
      const chartHeight = chart.height;

      ctx.restore();

      const creditRatingText = creditRating;
      ctx.font = `bold 1.5rem Pretendard`;
      ctx.textBaseline = 'bottom';
      ctx.fillStyle = '#242424';

      const creditRatingWidth = ctx.measureText(creditRatingText).width;
      const creditRatingX = (chartWidth - creditRatingWidth) / 2;
      const creditRatingY = chartHeight - 14;

      ctx.fillText(creditRatingText, creditRatingX, creditRatingY);

      const evaluationText = text;
      ctx.font = `lighter 0.625rem Pretendard`;
      ctx.fillStyle = '#8A8D8E';

      const evaluationTextWidth = ctx.measureText(evaluationText).width;
      const evaluationX = (chartWidth - evaluationTextWidth) / 2;
      const evaluationY = chartHeight - 2;

      ctx.fillText(evaluationText, evaluationX, evaluationY);

      ctx.save();
    },
  };

  return (
    <div className='w-[216px] h-[112px]'>
      <Doughnut data={data} options={options} plugins={[centerTextPlugin]} />
    </div>
  );
};

export default HalfDoughnutChart;

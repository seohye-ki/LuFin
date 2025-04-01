import { stockNews } from '../../../../types/Stock/stock';
import moment from 'moment';

const TodayNews = () => {
  return (
    <div className='flex flex-col gap-2'>
      {stockNews.map((news) => (
        <div
          key={news.stockNewsId}
          className='flex flex-col gap-2 p-3 border border-purple rounded-lg hover:bg-purple-30'
        >
          <span className='text-p1 font-semibold'>{news.content}</span>
          <span className='text-c1 text-grey'>{moment(news.createdAt).format('YYYY-MM-DD')}</span>
        </div>
      ))}
    </div>
  );
};

export default TodayNews;

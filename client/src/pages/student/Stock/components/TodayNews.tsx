import Card from '../../../../components/Card/Card';

const TodayNews = () => {
  return (
    <Card
      titleLeft='오늘의 뉴스'
      titleRight={
        <div className='flex items-center gap-2'>
          <div className='text-c1 text-grey'>
            모든 투자 결정과 그 결과에 대한 책임은 전적으로 투자자 본인에게 있습니다.
          </div>
        </div>
      }
      className='flex flex-col basis-55/100 min-h-0'
    >
      <div className='flex flex-col gap-2 flex-1 overflow-auto'>
        {/* {stockNews.map((news) => (
          <div
            key={news.stockNewsId}
            className='flex flex-col gap-2 p-3 border border-purple rounded-lg hover:bg-purple-30'
          >
            <span className='text-p1 font-semibold'>{news.content}</span>
            <span className='text-c1 text-grey'>{moment(news.createdAt).format('YYYY-MM-DD')}</span>
          </div>
        ))} */}
      </div>
    </Card>
  );
};

export default TodayNews;

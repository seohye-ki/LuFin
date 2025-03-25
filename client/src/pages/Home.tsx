import Profile from '../components/Profile/Profile';

const Home = () => {
  return (
    <div className='p-8 bg-broken-white min-h-screen'>
      <h1 className='text-h1 font-regular text-black mb-4'>Home Page</h1>
      <Profile
        name='이재현 선생님'
        profileImage='https://picsum.photos/200/300?random=1'
        certificationNumber='5학년 1반'
      />

      <div className='grid grid-cols-1 md:grid-cols-2 gap-6 mb-8'>
        <div className='bg-white p-6 rounded-lg shadow-md'>
          <h2 className='text-h2 font-semibold text-dark-grey mb-3'>Typography Test</h2>

          <p className='text-p1 font-regular mb-2'>This is p1 text - Regular</p>
          <p className='text-p1 font-medium mb-2'>This is p1 text - Medium</p>
          <p className='text-p1 font-semibold mb-2'>This is p1 text - Semibold</p>

          <p className='text-p2 font-regular mb-2'>This is p2 text - Regular</p>
          <p className='text-p2 font-medium mb-2'>This is p2 text - Medium</p>
          <p className='text-p2 font-semibold mb-2'>This is p2 text - Semibold</p>

          <p className='text-p3 font-regular mb-2'>This is p3 text - Regular</p>
          <p className='text-p3 font-medium mb-2'>This is p3 text - Medium</p>
          <p className='text-p3 font-semibold mb-2'>This is p3 text - Semibold</p>

          <p className='text-c1 font-regular mb-2'>This is caption 1 text - Regular</p>
          <p className='text-c1 font-medium mb-2'>This is caption 1 text - Medium</p>
          <p className='text-c2 font-light mb-2'>This is caption 2 text - Light</p>
          <p className='text-c2 font-regular mb-2'>This is caption 2 text - Regular</p>
        </div>

        <div className='bg-white p-6 rounded-lg shadow-md'>
          <h2 className='text-h2 font-bold text-black mb-3'>Color Test</h2>
          <div className='grid grid-cols-2 gap-4'>
            <div className='bg-light-cyan p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Cyan</span>
            </div>
            <div className='bg-purple p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Purple</span>
            </div>
            <div className='bg-yellow p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Yellow</span>
            </div>
            <div className='bg-pink p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Pink</span>
            </div>

            <div className='bg-success text-white p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Success</span>
            </div>
            <div className='bg-danger text-white p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Danger</span>
            </div>
            <div className='bg-info text-white p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Info</span>
            </div>
            <div className='bg-warning p-4 rounded flex items-center justify-center'>
              <span className='text-p2'>Warning</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;

const DesignSystem = () => {
  return (
    <div className='p-8 bg-broken-white min-h-screen'>
      <h1 className='text-h1 font-regular text-black mb-4'>Design System</h1>

      <div className='grid grid-cols-1 md:grid-cols-2 gap-6 mb-8'>
        <div className='bg-white p-6 rounded-lg shadow-md'>
          <h2 className='text-h2 font-semibold text-dark-grey mb-3'>Typography Test</h2>

          <div className='space-y-4'>
            <div>
              <h3 className='text-h3 font-medium mb-2'>Heading Styles</h3>
              <div className='mb-2'>
                <p className='text-h1 font-regular'>Heading 1 - Regular</p>
                <p className='text-c2 text-grey'>text-h1 font-regular</p>
              </div>
              <div className='mb-2'>
                <p className='text-h2 font-medium'>Heading 2 - Medium</p>
                <p className='text-c2 text-grey'>text-h2 font-medium</p>
              </div>
              <div className='mb-2'>
                <p className='text-h3 font-semibold'>Heading 3 - Semibold</p>
                <p className='text-c2 text-grey'>text-h3 font-semibold</p>
              </div>
            </div>

            <div>
              <h3 className='text-h3 font-medium mb-2'>Paragraph Styles</h3>
              <div className='mb-2'>
                <p className='text-p1 font-regular'>Paragraph 1 - Regular (16px)</p>
                <p className='text-c2 text-grey'>text-p1 font-regular</p>
              </div>
              <div className='mb-2'>
                <p className='text-p1 font-medium'>Paragraph 1 - Medium</p>
                <p className='text-c2 text-grey'>text-p1 font-medium</p>
              </div>
              <div className='mb-2'>
                <p className='text-p1 font-semibold'>Paragraph 1 - Semibold</p>
                <p className='text-c2 text-grey'>text-p1 font-semibold</p>
              </div>

              <div className='mb-2'>
                <p className='text-p2 font-regular'>Paragraph 2 - Regular (14px)</p>
                <p className='text-c2 text-grey'>text-p2 font-regular</p>
              </div>
              <div className='mb-2'>
                <p className='text-p2 font-medium'>Paragraph 2 - Medium</p>
                <p className='text-c2 text-grey'>text-p2 font-medium</p>
              </div>
              <div className='mb-2'>
                <p className='text-p2 font-semibold'>Paragraph 2 - Semibold</p>
                <p className='text-c2 text-grey'>text-p2 font-semibold</p>
              </div>
            </div>

            <div>
              <h3 className='text-h3 font-medium mb-2'>Caption Styles</h3>
              <div className='mb-2'>
                <p className='text-c1 font-regular'>Caption 1 - Regular (12px)</p>
                <p className='text-c2 text-grey'>text-c1 font-regular</p>
              </div>
              <div className='mb-2'>
                <p className='text-c1 font-medium'>Caption 1 - Medium</p>
                <p className='text-c2 text-grey'>text-c1 font-medium</p>
              </div>
              <div className='mb-2'>
                <p className='text-c2 font-light'>Caption 2 - Light (10px)</p>
                <p className='text-c2 text-grey'>text-c2 font-light</p>
              </div>
              <div className='mb-2'>
                <p className='text-c2 font-regular'>Caption 2 - Regular</p>
                <p className='text-c2 text-grey'>text-c2 font-regular</p>
              </div>
            </div>
          </div>
        </div>

        <div className='bg-white p-6 rounded-lg shadow-md'>
          <h2 className='text-h2 font-bold text-black mb-3'>Color Test</h2>
          <div className='grid grid-cols-2 gap-4'>
            <div>
              <h3 className='text-h3 font-medium mb-2'>Regular Colors</h3>
              <div className='grid grid-cols-2 gap-2'>
                <div className='bg-white border border-grey-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>White</span>
                  <span className='text-c2 text-grey'>bg-white</span>
                </div>
                <div className='bg-broken-white border border-grey-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Broken White</span>
                  <span className='text-c2 text-grey'>bg-broken-white</span>
                </div>
                <div className='bg-new-grey p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>New Grey</span>
                  <span className='text-c2 text-grey'>bg-new-grey</span>
                </div>
                <div className='bg-grey-25 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Grey 25</span>
                  <span className='text-c2 text-grey'>bg-grey-25</span>
                </div>
                <div className='bg-grey-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Grey 30</span>
                  <span className='text-c2 text-grey'>bg-grey-30</span>
                </div>
                <div className='bg-grey p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Grey</span>
                  <span className='text-c2 text-white/70'>bg-grey</span>
                </div>
                <div className='bg-dark-grey p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Dark Grey</span>
                  <span className='text-c2 text-white/70'>bg-dark-grey</span>
                </div>
                <div className='bg-black p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Black</span>
                  <span className='text-c2 text-white/70'>bg-black</span>
                </div>
              </div>
            </div>

            <div>
              <h3 className='text-h3 font-medium mb-2'>Main Colors</h3>
              <div className='grid grid-cols-2 gap-2'>
                <div className='bg-light-cyan p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Light Cyan</span>
                  <span className='text-c2 text-grey'>bg-light-cyan</span>
                </div>
                <div className='bg-light-cyan-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Light Cyan 30</span>
                  <span className='text-c2 text-grey'>bg-light-cyan-30</span>
                </div>
                <div className='bg-purple p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Purple</span>
                  <span className='text-c2 text-grey'>bg-purple</span>
                </div>
                <div className='bg-purple-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Purple 30</span>
                  <span className='text-c2 text-grey'>bg-purple-30</span>
                </div>
                <div className='bg-yellow p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Yellow</span>
                  <span className='text-c2 text-grey'>bg-yellow</span>
                </div>
                <div className='bg-yellow-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Yellow 30</span>
                  <span className='text-c2 text-grey'>bg-yellow-30</span>
                </div>
              </div>
            </div>

            <div>
              <h3 className='text-h3 font-medium mb-2'>Semantic Colors</h3>
              <div className='grid grid-cols-2 gap-2'>
                <div className='bg-success p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Success</span>
                  <span className='text-c2 text-white/70'>bg-success</span>
                </div>
                <div className='bg-danger p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Danger</span>
                  <span className='text-c2 text-white/70'>bg-danger</span>
                </div>
                <div className='bg-info p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Info</span>
                  <span className='text-c2 text-white/70'>bg-info</span>
                </div>
                <div className='bg-warning p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Warning</span>
                  <span className='text-c2 text-grey'>bg-warning</span>
                </div>
              </div>
            </div>

            <div>
              <h3 className='text-h3 font-medium mb-2'>Secondary Colors</h3>
              <div className='grid grid-cols-2 gap-2'>
                <div className='bg-dark-purple p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Dark Purple</span>
                  <span className='text-c2 text-white/70'>bg-dark-purple</span>
                </div>
                <div className='bg-dark-pink p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2 text-white'>Dark Pink</span>
                  <span className='text-c2 text-white/70'>bg-dark-pink</span>
                </div>
                <div className='bg-pink p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Pink</span>
                  <span className='text-c2 text-grey'>bg-pink</span>
                </div>
                <div className='bg-pink-30 p-2 rounded flex flex-col items-center justify-center'>
                  <span className='text-p2'>Pink 30</span>
                  <span className='text-c2 text-grey'>bg-pink-30</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DesignSystem; 
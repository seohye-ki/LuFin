const Awning = () => {
  return (
    <div className='w-full h-fit flex flex-row overflow-x-clip'>
      {Array.from({ length: 100 }).map((_, index) => (
        <div
          key={index}
          className={`w-5 h-14 rounded-b-full flex-shrink-0 ${index % 2 === 0 ? 'bg-light-cyan' : 'bg-yellow'}`}
        ></div>
      ))}
    </div>
  );
};

export default Awning;

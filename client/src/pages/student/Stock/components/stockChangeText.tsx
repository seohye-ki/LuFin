const StockChangeText = ({ diff, rate }: { diff: number; rate: number }) => {
  const isUp = diff > 0;
  const color = isUp ? 'text-danger' : 'text-info';
  const sign = isUp ? '+' : '';
  return (
    <span className={color}>{`${sign}${diff.toLocaleString()}루핀 (${rate.toFixed(2)}%)`}</span>
  );
};

export default StockChangeText;

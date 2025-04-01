export const calcProfitRate = (totalValue: number, currentValue: number) => {
  const profitValue = currentValue - totalValue;
  const profitRate = (profitValue / totalValue) * 100;
  return {
    profitValue,
    profitRate: isNaN(profitRate) ? 0 : parseFloat(profitRate.toFixed(2)),
  };
};

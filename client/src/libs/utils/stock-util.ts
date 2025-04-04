export const calcProfitRate = (totalValue: number, currentValue: number) => {
  const profitValue = currentValue - totalValue;
  const profitRate = (profitValue / totalValue) * 100;
  return {
    profitValue,
    profitRate: isNaN(profitRate) ? 0 : parseFloat(profitRate.toFixed(2)),
  };
};

export const calcStockChangeRate = (currentPrice: number, initialPrice: number) => {
  const diff = currentPrice - initialPrice;
  const rate = (diff / initialPrice) * 100;
  return {
    diff,
    rate: isNaN(rate) ? 0 : parseFloat(rate.toFixed(2)),
  };
};

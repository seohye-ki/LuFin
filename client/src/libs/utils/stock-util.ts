import { StockPortfolio, StockProduct } from '../../types/stock/stock';

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

export const buildPortfolioDetails = (portfolio: StockPortfolio[], products: StockProduct[]) => {
  return portfolio
    .map((p) => {
      const product = products.find((product) => product.StockProductId === p.StockProductId);
      if (!product) return null;

      const averagePrice = p.Quantity > 0 ? p.TotalPurchaseAmount / p.Quantity : 0;
      const currentValue = p.Quantity * product.CurrentPrice;
      const { profitValue, profitRate } = calcProfitRate(averagePrice, currentValue);

      return {
        stock: product.Name,
        quantity: p.Quantity,
        amount: averagePrice,
        currentValue,
        profit: profitValue,
        profitRate,
      };
    })
    .filter((item): item is NonNullable<typeof item> => item !== null);
};

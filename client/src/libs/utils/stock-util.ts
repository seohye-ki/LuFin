import { StockPortfolio, StockProduct } from '../../types/stock/stock';

export const calcProfitRate = (totalPrice: number, currentValue: number) => {
  const profitValue = currentValue - totalPrice;
  const profitRate = (profitValue / totalPrice) * 100;
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
      const product = products.find((product) => product.stockProductId === p.stockProductId);
      if (!product) return null;

      const averagePrice = p.quantity > 0 ? p.totalPurchaseAmount / p.quantity : 0;
      const currentValue = p.quantity * product.currentPrice;
      const { profitValue, profitRate } = calcProfitRate(averagePrice, currentValue);

      return {
        stock: product.name,
        quantity: p.quantity,
        amount: averagePrice,
        currentValue,
        profit: profitValue,
        profitRate,
      };
    })
    .filter((item): item is NonNullable<typeof item> => item !== null);
};

import { useEffect } from 'react';
import { useSelectedStockStore, useStockStore } from '../../../../libs/store/stockStore';
import { StockProduct } from '../../../../types/stock/stock';

export const useStock = () => {
  const {
    getStockProducts,
    getStockPriceHistory,
    getStockNews,
    getStockPortfolio,
    postStockTransaction,
    getStockStatus,
  } = useStockStore();

  const { selectedStock, setSelectedStock, chartType, toggleChartType } = useSelectedStockStore();

  const { products, selectedProductId, priceHistory, news, portfolio, isLoading, error } =
    getStockStatus();

  /**
   * 종목 선택 시 상세 정보 가져오기
   */
  const selectStock = async (stock: StockProduct) => {
    setSelectedStock(stock);
    await Promise.all([
      getStockPriceHistory(stock.StockProductId),
      getStockNews(stock.StockProductId),
    ]);
  };

  /**
   * 전체 종목 초기 불러오기
   */
  useEffect(() => {
    getStockProducts();
  }, []);

  return {
    // 상태
    products,
    selectedStock,
    selectedProductId,
    priceHistory,
    news,
    portfolio,
    isLoading,
    error,
    chartType,

    // 액션
    selectStock,
    setSelectedStock,
    toggleChartType,
    getStockPortfolio,
    postStockTransaction,
  };
};

import { create } from 'zustand';
import {
  StockProduct,
  StockPriceHistory,
  StockNews,
  StockPortfolio,
} from '../../types/stock/stock';
import { stockService } from '../services/stock/stockService';

type ChartType = 'realTime' | 'portfolio';

interface StockState {
  products: StockProduct[];
  selectedProductId: number | null;
  priceHistory: StockPriceHistory[];
  news: StockNews[];
  portfolio: StockPortfolio[];
  isLoading: boolean;
  error: string | null;
  selectedStock: StockProduct | null;

  getStockProducts: () => Promise<{ success: boolean; message?: string }>;
  getStockPriceHistory: (
    productId: number,
    day?: number,
  ) => Promise<{ success: boolean; message?: string }>;
  postStockTransaction: (
    productId: number,
    type: 'BUY' | 'SELL',
    quantity: number,
    price: number,
  ) => Promise<{ success: boolean; transactionId?: number; message?: string }>;
  getStockNews: () => Promise<{ success: boolean; message?: string }>;
  getStockPortfolio: () => Promise<{ success: boolean; message?: string }>;

  getStockStatus: () => {
    products: StockProduct[];
    selectedProductId: number | null;
    priceHistory: StockPriceHistory[];
    news: StockNews[];
    portfolio: StockPortfolio[];
    isLoading: boolean;
    error: string | null;
  };

  resetStockState: () => void;
}

interface StockStore {
  selectedStock: StockProduct | null;
  chartType: ChartType;
  setSelectedStock: (stock: StockProduct | null) => void;
  toggleChartType: () => void;
}

export const useSelectedStockStore = create<StockStore>((set) => ({
  selectedStock: null,
  chartType: 'realTime',
  setSelectedStock: (stock) => set({ selectedStock: stock }),
  toggleChartType: () =>
    set((state) => ({
      chartType: state.chartType === 'realTime' ? 'portfolio' : 'realTime',
    })),
}));

export const useStockStore = create<StockState>((set, get) => ({
  products: [],
  selectedProductId: null,
  priceHistory: [],
  news: [],
  portfolio: [],
  isLoading: false,
  error: null,
  selectedStock: null,

  /**
   * 종목 목록 조회
   */
  getStockProducts: async () => {
    set({ isLoading: true });
    try {
      const result = await stockService.getStockProducts();
      if (result.success) {
        set({
          products: result.products,
          isLoading: false,
        });
        return { success: true };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('종목 목록 조회 오류:', error);
      set({ isLoading: false, error: '종목 목록 조회 오류' });
      return { success: false, message: '종목 목록 조회 오류' };
    }
  },
  /**
   * 종목 가격 변동 이력 조회
   */
  getStockPriceHistory: async (productId: number) => {
    set({ isLoading: true });
    try {
      const result = await stockService.getStockPriceHistory(productId);
      if (result.success) {
        set({
          priceHistory: result.history,
          selectedProductId: productId,
          isLoading: false,
        });
        return { success: true };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('종목 가격 변동 이력 조회 오류:', error);
      set({ isLoading: false, error: '종목 가격 변동 이력 조회 오류' });
      return { success: false, message: '종목 가격 변동 이력 조회 오류' };
    }
  },
  /**
   * 종목 매수/매도
   */
  postStockTransaction: async (
    productId,
    type,
    quantity,
    price,
  ): Promise<{ success: boolean; transactionId?: number; message?: string }> => {
    set({ isLoading: true });
    try {
      const result = await stockService.postStockTransaction(productId, {
        stockProductId: productId,
        type: type === 'BUY' ? 1 : 0,
        quantity: quantity,
        price: price,
        totalPrice: quantity * price,
      });
      if (result.success) {
        set({ isLoading: false });
        return { success: true, transactionId: result.transactionId };
      } else {
        set({ isLoading: false });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('종목 매수/매도 오류:', error);
      set({ isLoading: false, error: '종목 매수/매도 오류' });
      return { success: false, message: '종목 매수/매도 오류' };
    }
  },
  /**
   * 종목 공시 정보 조회
   */
  getStockNews: async () => {
    set({ isLoading: true });
    try {
      const result = await stockService.getStockNews();
      if (result.success) {
        set({
          news: result.news,
          isLoading: false,
        });
        return { success: true };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('종목 공시 정보 조회 오류:', error);
      set({ isLoading: false, error: '종목 공시 정보 조회 오류' });
      return { success: false, message: '종목 공시 정보 조회 오류' };
    }
  },
  /**
   * 투자 포트폴리오 조회
   */
  getStockPortfolio: async () => {
    set({ isLoading: true });
    try {
      const result = await stockService.getStockPortfolio();
      if (result.success) {
        set({
          portfolio: result.portfolio,
          isLoading: false,
        });
        return { success: true };
      } else {
        set({ isLoading: false, error: result.message || null });
        return { success: false, message: result.message };
      }
    } catch (error) {
      console.error('투자 포트폴리오 조회 오류:', error);
      set({ isLoading: false, error: '투자 포트폴리오 조회 오류' });
      return { success: false, message: '투자 포트폴리오 조회 오류' };
    }
  },
  /**
   * 상태 조회
   */
  getStockStatus: () => {
    const { products, selectedProductId, priceHistory, news, portfolio, isLoading, error } = get();
    return {
      products,
      selectedProductId,
      priceHistory,
      news,
      portfolio,
      isLoading,
      error,
    };
  },
  /**
   * 상태 초기화
   */
  resetStockState: () => {
    set({
      selectedProductId: null,
      priceHistory: [],
      news: [],
      isLoading: false,
      error: null,
    });
  },
}));

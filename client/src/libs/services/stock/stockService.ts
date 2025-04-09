import axiosInstance from '../axios';
import {
  StockProduct,
  StockPriceHistory,
  StockNews,
  StockTransactionRequest,
  StockTransactionResponse,
  StockPortfolio,
} from '../../../types/stock/stock';

export interface StockResponse<T> {
  isSuccess: boolean;
  data?: T;
  code?: string;
  message?: string;
}

const STOCK_ENDPOINT = '/stocks';

export const stockService = {
  /**
   * 투자 상품 목록 조회
   */
  getStockProducts: async () => {
    try {
      const response = await axiosInstance.get<StockResponse<StockProduct[]>>(
        `${STOCK_ENDPOINT}/products`,
      );

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          products: response.data.data,
        };
      } else {
        return {
          success: false,
          message: response.data.message || '투자 상품 목록 조회 실패',
        };
      }
    } catch (error) {
      console.error('투자 상품 목록 조회 오류:', error);
      return {
        success: false,
        message: '투자 상품 목록 조회 오류',
      };
    }
  },
  /**
   * 주식 가격 변동 이력 조회
   */
  getStockPriceHistory: async (productId: number, day: number = 7) => {
    try {
      const response = await axiosInstance.get<StockResponse<StockPriceHistory[]>>(
        `${STOCK_ENDPOINT}/products/${productId}/price-history?day=${day}`,
      );

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          history: response.data.data,
        };
      } else {
        return {
          success: false,
          message: response.data.message || '주식 가격 변동 이력 조회 실패',
        };
      }
    } catch (error) {
      console.error('주식 가격 변동 이력 조회 오류:', error);
      return {
        success: false,
        message: '주식 가격 변동 이력 조회 오류',
      };
    }
  },
  /**
   * 투자 상품 매수/매도
   */
  postStockTransaction: async (productId: number, data: StockTransactionRequest) => {
    try {
      const response = await axiosInstance.post<StockResponse<StockTransactionResponse>>(
        `${STOCK_ENDPOINT}/products/${productId}/`,
        data,
      );

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          transactionId: response.data.data.StockHistoryId,
        };
      } else {
        return {
          success: false,
          message: response.data.message || '투자 상품 매수/매도 실패',
        };
      }
    } catch (error) {
      console.error('투자 상품 매수/매도 오류:', error);
      return {
        success: false,
        message: '투자 상품 매수/매도 오류',
      };
    }
  },
  /**
   * 공시 정보 조회
   */
  getStockNews: async (productId: number) => {
    try {
      const response = await axiosInstance.get<StockResponse<StockNews[]>>(
        `${STOCK_ENDPOINT}/products/${productId}/news`,
      );

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          news: response.data.data,
        };
      } else {
        return {
          success: false,
          message: response.data.message || '공시 정보 조회 실패',
        };
      }
    } catch (error) {
      console.error('공시 정보 조회 오류:', error);
      return {
        success: false,
        message: '공시 정보 조회 오류',
      };
    }
  },
  /**
   * 투자 포트폴리오 조회
   */
  getStockPortfolio: async () => {
    try {
      const response = await axiosInstance.get<StockResponse<StockPortfolio[]>>(
        `${STOCK_ENDPOINT}/portfolios`,
      );

      if (response.data.isSuccess && response.data.data) {
        return {
          success: true,
          portfolio: response.data.data,
        };
      } else {
        return {
          success: false,
          message: response.data.message || '투자 포트폴리오 조회 실패',
        };
      }
    } catch (error) {
      console.error('투자 포트폴리오 조회 오류:', error);
      return {
        success: false,
        message: '투자 포트폴리오 조회 오류',
      };
    }
  },
};

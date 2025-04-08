export interface StockProduct {
  stockProductId: number;
  name: string;
  description: string;
  initialPrice: number;
  currentPrice: number;
  createdAt: string;
  updatedAt: string;
}

export interface StockPriceHistory {
  stockHistoryId: number;
  stockProductId: number;
  price: number;
  createdAt: string;
}

export interface StockNews {
  stockProductId: number;
  content: string;
  createdAt: string;
  updatedAt: string;
}

export interface StockTransactionRequest {
  stockProductId: number;
  type: 0 | 1; // 0: 판매, 1: 구매
  quantity: number;
  price: number;
  totalPrice: number;
}

export interface StockTransactionResponse {
  stockHistoryId: number;
}

export interface StockPortfolio {
  stockProductId: number;
  quantity: number;
  totalPurchaseAmount: number;
  totalSellAmount: number;
  totalReturn: number;
  totalReturnRate: number;
  createdAt: string;
  updatedAt: string;
}

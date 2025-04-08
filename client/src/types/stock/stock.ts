// types/stock.ts

export interface StockProduct {
  StockProductId: number;
  Name: string;
  Description: string;
  InitialPrice: number;
  CurrentPrice: number;
  CreatedAt: string;
  UpdatedAt: string;
}

export interface StockPriceHistory {
  StockHistoryId: number;
  StockProductId: number;
  UnitPrice: number;
  CreatedAt: string;
}

export interface StockNews {
  StockProductId: number;
  Content: string;
  CreatedAt: string;
  UpdatedAt: string;
}

export interface StockTransactionRequest {
  StockProductId: number;
  Type: 0 | 1; // 0: 판매, 1: 구매
  Quantity: number;
  UnitPrice: number;
  TotalValue: number;
}

export interface StockTransactionResponse {
  StockHistoryId: number;
}

export interface StockPortfolio {
  StockProductId: number;
  Quantity: number;
  TotalPurchaseAmount: number;
  TotalSellAmount: number;
  TotalReturn: number;
  TotalReturnRate: number;
  CreatedAt: string;
  UpdatedAt: string;
}

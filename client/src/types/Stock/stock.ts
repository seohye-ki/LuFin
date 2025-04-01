// 투자 상품 (주식)
export interface StockProduct {
  stockProductId: number;
  name: string; // 투자 상품명
  description: string; // 투자 상품 설명
  initialPrice: number; // 최초 공시 가격
  currentPrice: number; // 현재 가격
  createdAt: string; // 생성 일자
  updatedAt: string; // 변경 일자자
}

// 주식 뉴스 (공시 정보)
export interface StockNews {
  stockNewsId: number;
  stockProductId: number; // 투자 상품 고유 번호
  content: string; // 공시 정보 내용
  createdAt: string; // 정보 공시 일자
  updatedAt: string; // 변경 일자자
}

// 가격 변동 기록
export interface StockPriceHistory {
  stockPriceId: number;
  stockProductId: number; // 투자 상품 고유 번호
  unitPrice: number; // 공시 당시 단가
  createdAt: string; // 공시 일자
  updatedAt: string; // 변경 일자자
}

// 주식 포트폴리오
export interface StockPortfolio {
  stockPortfolioId: number;
  stockProductId: number; // 투자 상품 고유 번호
  memberId: number; // 학생 고유 번호
  quantity: number; // 한 투자 상품에 대한 총 보유 수량
  totalPurchaseAmount: number; // 한 투자 상품에 대한 총 매수 금액
  totalSellAmount: number; // 한 투자 상품에 대한 총 매도 금액
  createdAt: string;
  updatedAt: string; // 최종 거래 일자자
}

// 주식 거래 내역
export interface StockHistory {
  investmentId: number;
  stockProductId: number; // 투자 상품 고유 번호
  memberId: number;
  type: 0 | 1; // 0: 매수, 1: 매도
  quantity: number; // 수량
  unitPrice: number; // 거래 당시 단가
  totalValue: number; /// 거래 당시 총 가치
  createdAt: string; // 생성 일자
  updatedAt: string; // 변경 일자
}

export const stockPriceHistories: StockPriceHistory[] = [
  {
    stockPriceId: 1,
    stockProductId: 1,
    unitPrice: 12500,
    createdAt: '2025-03-28',
    updatedAt: '2025-03-28',
  },
  {
    stockPriceId: 2,
    stockProductId: 1,
    unitPrice: 13000,
    createdAt: '2025-03-29',
    updatedAt: '2025-03-29',
  },
  {
    stockPriceId: 3,
    stockProductId: 1,
    unitPrice: 11500,
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockPriceId: 2,
    stockProductId: 2,
    unitPrice: 9500,
    createdAt: '2025-03-29',
    updatedAt: '2025-03-29',
  },
  {
    stockPriceId: 3,
    stockProductId: 3,
    unitPrice: 10800,
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockPriceId: 4,
    stockProductId: 4,
    unitPrice: 11500,
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockPriceId: 5,
    stockProductId: 5,
    unitPrice: 8500,
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
];

export const stockPortfolios: StockPortfolio[] = [
  {
    stockPortfolioId: 1,
    stockProductId: 1,
    memberId: 2,
    quantity: 7,
    totalPurchaseAmount: 70000,
    totalSellAmount: 0,
    createdAt: '2025-03-28',
    updatedAt: '2025-03-30',
  },
  {
    stockPortfolioId: 2,
    stockProductId: 5,
    memberId: 2,
    quantity: 5,
    totalPurchaseAmount: 600000,
    totalSellAmount: 0,
    createdAt: '2025-03-29',
    updatedAt: '2025-03-30',
  },
];

export const stockHistories: StockHistory[] = [
  {
    investmentId: 1,
    stockProductId: 1,
    memberId: 101,
    type: 0,
    quantity: 5,
    unitPrice: 10000,
    totalValue: 50000,
    createdAt: '2025-03-27',
    updatedAt: '2025-03-27',
  },
  {
    investmentId: 2,
    stockProductId: 1,
    memberId: 101,
    type: 1,
    quantity: 2,
    unitPrice: 11000,
    totalValue: 22000,
    createdAt: '2025-03-28',
    updatedAt: '2025-03-28',
  },
  {
    investmentId: 3,
    stockProductId: 2,
    memberId: 102,
    type: 0,
    quantity: 5,
    unitPrice: 10000,
    totalValue: 50000,
    createdAt: '2025-03-22',
    updatedAt: '2025-03-22',
  },
  {
    investmentId: 4,
    stockProductId: 3,
    memberId: 103,
    type: 0,
    quantity: 8,
    unitPrice: 11000,
    totalValue: 88000,
    createdAt: '2025-03-25',
    updatedAt: '2025-03-25',
  },
  {
    investmentId: 5,
    stockProductId: 3,
    memberId: 103,
    type: 1,
    quantity: 1,
    unitPrice: 11000,
    totalValue: 11000,
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
];

export const stockProducts: StockProduct[] = [
  {
    stockProductId: 1,
    name: '삼성전자',
    description: '삼성전자 주식 상품',
    initialPrice: 10000,
    currentPrice: 13000,
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
  },
  {
    stockProductId: 2,
    name: '현대차',
    description: '현대차 주식 상품',
    initialPrice: 10000,
    currentPrice: 9000,
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
  },
  {
    stockProductId: 3,
    name: 'LG전자',
    description: 'LG전자 주식 상품',
    initialPrice: 10000,
    currentPrice: 11000,
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
  },
  {
    stockProductId: 4,
    name: '네이버',
    description: '네이버 주식 상품',
    initialPrice: 10000,
    currentPrice: 12000,
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
  },
  {
    stockProductId: 5,
    name: '카카오',
    description: '카카오 주식 상품',
    initialPrice: 10000,
    currentPrice: 8000,
    createdAt: '2021-01-01',
    updatedAt: '2021-01-01',
  },
];

export const stockNews: StockNews[] = [
  {
    stockNewsId: 1,
    stockProductId: 1,
    content: '반도체 생산 시설 화재로 글로벌 공급망 차질 예상',
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockNewsId: 2,
    stockProductId: 1,
    content: '글로벌 IT 대기업, 인공지능 기술 개발 협력 발표',
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockNewsId: 3,
    stockProductId: 1,
    content: '클라우드 보안 취약점 발견으로 기업 데이터 유출 우려',
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockNewsId: 4,
    stockProductId: 1,
    content: '친환경 배터리 기술 혁신으로 전기차 주행거리 2배 확대',
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
  {
    stockNewsId: 5,
    stockProductId: 1,
    content: '원격 의료 플랫폼, 인공지능 진단 시스템 도입으로 정확도 향상',
    createdAt: '2025-03-30',
    updatedAt: '2025-03-30',
  },
];

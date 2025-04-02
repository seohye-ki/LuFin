import { create } from 'zustand';
import { StockProduct } from '../../types/stock/stock';

type ChartType = 'realTime' | 'portfolio';

interface StockStore {
  selectedStock: StockProduct | null;
  chartType: ChartType;
  setSelectedStock: (stock: StockProduct | null) => void;
  toggleChartType: () => void;
}

export const useStockStore = create<StockStore>((set) => ({
  selectedStock: null,
  chartType: 'realTime',
  setSelectedStock: (stock: StockProduct | null) => set({ selectedStock: stock }),
  toggleChartType: () =>
    set((state) => ({ chartType: state.chartType === 'realTime' ? 'portfolio' : 'realTime' })),
}));

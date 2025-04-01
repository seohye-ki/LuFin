import { create } from 'zustand';
import { StockProduct } from '../../types/Stock/stock';

type ChartType = 'realTime' | 'portfolio';

interface StockStore {
  selectedStock: StockProduct | null;
  chartType: ChartType;
  setSelectedStock: (stock: StockProduct) => void;
  toggleChartType: () => void;
}

export const useStockStore = create<StockStore>((set) => ({
  selectedStock: null,
  chartType: 'realTime',
  setSelectedStock: (stock: StockProduct) => set({ selectedStock: stock }),
  toggleChartType: () =>
    set((state) => ({ chartType: state.chartType === 'realTime' ? 'portfolio' : 'realTime' })),
}));

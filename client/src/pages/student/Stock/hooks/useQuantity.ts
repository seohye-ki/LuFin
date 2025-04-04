import { useState, useCallback } from 'react';

interface UseQuantityReturn {
  quantity: number;
  increase: () => void;
  decrease: () => void;
  setByPercent: (percent: number) => void;
}

export const useQuantity = (max: number): UseQuantityReturn => {
  const [quantity, setQuantity] = useState(0);

  const increase = useCallback(() => {
    setQuantity((prev) => Math.min(prev + 1, max));
  }, [max]);

  const decrease = useCallback(() => {
    setQuantity((prev) => Math.max(prev - 1, 0));
  }, []);

  const setByPercent = useCallback(
    (percent: number) => {
      const newQuantity = Math.floor((max * percent) / 100);
      setQuantity(newQuantity);
    },
    [max],
  );

  return {
    quantity,
    increase,
    decrease,
    setByPercent,
  };
};

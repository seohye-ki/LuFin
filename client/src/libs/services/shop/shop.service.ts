import { ItemDTO } from '../../../types/shop/item';
import axiosInstance from '../axios';

export const getMyItemList = async (): Promise<ItemDTO[]> => {
  const response = await axiosInstance.get<{ data: ItemDTO[] }>('/items/inventory');
  return response.data.data;
};

export const getSalesItemList = async (): Promise<ItemDTO[]> => {
  const response = await axiosInstance.get<{ data: ItemDTO[] }>('/items');
  return response.data.data;
};

export const requestUseItem = async (purchaseId: number): Promise<boolean> => {
  const response = await axiosInstance.post(`/items/purchases/${purchaseId}/request`);
  return response.data.isSuccess;
};

export const buyItem = async (itemId: number): Promise<boolean> => {
  const response = await axiosInstance.post('/items/purchase', { itemId, itemCount: 1 });
  return response.data.isSuccess;
};

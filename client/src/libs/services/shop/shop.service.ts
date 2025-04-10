import { ItemDTO, ItemMemberDTO, ItemRequestDTO } from '../../../types/shop/item';
import axiosInstance from '../axios';

export const getMyItemList = async (): Promise<ItemDTO[]> => {
  const response = await axiosInstance.get<{ data: ItemDTO[] }>('/items/inventory');
  return response.data.data;
};

export const getSalesItemList = async (): Promise<ItemDTO[]> => {
  const response = await axiosInstance.get<{ data: ItemDTO[] }>('/items');
  return response.data.data;
};

export const getItemRequestList = async (): Promise<ItemRequestDTO[]> => {
  const response = await axiosInstance.get<{ data: ItemRequestDTO[] }>('/items/requests');
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

export const getBoughtMemberList = async (itemId: number): Promise<ItemMemberDTO[]> => {
  console.log(itemId);
  const response = await axiosInstance.get(`/items/${itemId}/purchases`);
  return response.data.data;
};

export const addItem = async (
  name: string,
  price: number,
  quantityAvailable: number,
  expirationDate: string,
): Promise<boolean> => {
  const response = await axiosInstance.post('/items', {
    name,
    type: 0,
    price,
    quantityAvailable,
    expirationDate,
  });

  return response.data.isSuccess;
};

export const deleteItem = async (itemId: number) => {
  await axiosInstance.delete(`/items/${itemId}`);
};

export const approveItemRequest = async (requestId: number, isApproved: boolean) => {
  await axiosInstance.patch(`/items/requests/${requestId}`, {
    status: isApproved ? 'APPROVED' : 'REJECTED',
  });
};

export type ItemDTO = {
  itemId: number;
  itemName: string;
  purchaseId: number;
  type: number;
  price: number;
  quantityAvailable: number;
  quantitySold: number;
  status: boolean;
  expirationDate: string;
};

export type ItemListDTO = ItemDTO[];

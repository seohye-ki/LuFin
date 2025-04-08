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

export type ItemMemberDTO = {
  itemId: number;
  itemName: string;
  memberId: number;
  memberName: string;
  memberProfileImage: string;
  status: string;
};

export type ItemRequestDTO = ItemMemberDTO & {
  requestId: number;
};

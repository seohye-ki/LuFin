// import React, { useEffect, useState } from 'react';
// import Card from '../../../../components/Card/Card';
// import Lufin from '../../../../components/Lufin/Lufin';
// import Profile from '../../../../components/Profile/Profile';

// import useAlertStore from '../../../../libs/store/alertStore';
// import Button from '../../../../components/Button/Button';

// const ItemDetailModal: React.FC<{ item: ItemPreview; closeModal: () => void }> = ({
//   item,
//   closeModal,
// }) => {
//   const { showAlert, hideAlert } = useAlertStore((state) => state);
//   const [itemDetail, setItemDetail] = useState<ItemDetail | null>(null);

//   useEffect(() => {
//     const itemDetailData = initialItemDetails.find((i) => item.itemId === i.itemId);

//     if (!itemDetailData) {
//       showAlert('아이템 정보를 찾을 수 없습니다.', null, '다시 시도해주세요.', 'danger', {
//         color: 'danger',
//         label: '확인',
//         onClick: () => {
//           hideAlert();
//         },
//       });

//       return;
//     }
//     setItemDetail(itemDetailData);
//   }, [item.itemId, showAlert, hideAlert]);

//   return (
//     itemDetail && (
//       <div
//         className='fixed top-0 left-0 right-0 bottom-0 flex justify-center items-center bg-black/80 z-50'
//         onClick={closeModal}
//       >
//         <div onClick={(e) => e.stopPropagation()}>
//           <Card className='w-104 h-fit' titleLeft={item.name}>
//             <div className='flex flex-col gap-6'>
//               <ItemDetailSection label='가격'>
//                 <Lufin size='s' count={item.price} />
//               </ItemDetailSection>

//               <ItemDetailSection label='기한'>
//                 <p className='text-p1'>{item.expirationDate.formattedDate}</p>
//               </ItemDetailSection>

//               <ItemDetailSection label='보유중'>
//                 <ProfileList students={itemDetail.buy} message='사용중인 학생이 없습니다.' />
//               </ItemDetailSection>

//               <ItemDetailSection label='사용요청'>
//                 <ProfileList students={itemDetail.request} message='사용요청한 학생이 없습니다.' />
//               </ItemDetailSection>

//               <ItemDetailSection label='사용완료'>
//                 <ProfileList students={itemDetail.used} message='사용완료한 학생이 없습니다.' />
//               </ItemDetailSection>

//               <ItemDetailSection label='기간만료'>
//                 <ProfileList students={itemDetail.expired} message='기간 만료한 학생이 없습니다.' />
//               </ItemDetailSection>
//             </div>
//             <Button onClick={closeModal}>확인</Button>
//           </Card>
//         </div>
//       </div>
//     )
//   );
// };

// const ItemDetailSection: React.FC<{ label: string; children: React.ReactNode }> = ({
//   label,
//   children,
// }) => {
//   return (
//     <div className='flex flex-col gap-2'>
//       <p className='text-p2 font-semibold text-grey'>{label}</p>
//       {children}
//     </div>
//   );
// };

// const ProfileList: React.FC<{ students: StudentPreview[]; message: string }> = ({
//   students,
//   message,
// }) => {
//   if (students.length === 0) {
//     return <p>{message}</p>;
//   }

//   return (
//     <div className='flex flex-row gap-4'>
//       {students.map((student) => (
//         <Profile
//           key={student.memberId}
//           name={student.name}
//           profileImage={student.profileImage}
//           variant='column'
//         />
//       ))}
//     </div>
//   );
// };

// export default ItemDetailModal;

import { useState } from 'react';
import Card from '../../../components/Card/Card';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { sampleLoanApplications, sampleLoanProducts } from '../../../types/Loan/loan';
import LoanProduct from './components/LoanProduct';
import LoanApplicationList from './components/LoanApplicationList';
import ApplyLoan from './components/ApplyLoan';
import DetailLoanApplication from './components/DetailLoanApplication';
import { LoanProductDTO } from '../../../types/Loan/loan';
import Lufin from '../../../components/Lufin/Lufin';

const StudentLoan = () => {
  const [selectedLoanProduct, setSelectedLoanProduct] = useState<LoanProductDTO | null>(null);
  const [selectedLoanId, setSelectedLoanId] = useState<number | null>(null); // 타입 수정

  return (
    <div>
      {/* ApplyLoan 모달 (대출 상품이 선택되었을 때 표시) */}
      {selectedLoanProduct && (
        <ApplyLoan
          loanProduct={selectedLoanProduct}
          closeModal={() => setSelectedLoanProduct(null)}
        />
      )}

      {/* DetailLoanApplication 모달 (대출 내역에서 선택되었을 때 표시) */}
      {selectedLoanId !== null && ( // 타입 체크 추가
        <DetailLoanApplication loanId={selectedLoanId} closeModal={() => setSelectedLoanId(null)} />
      )}

      <SidebarLayout>
        <div className='w-full h-full flex flex-col gap-4'>
          <div className='flex flex-row gap-4'>
            <Card titleLeft='대출 금액' titleSize='s' className='w-full'>
              <Lufin size='l' count={20000} />
            </Card>
            <Card titleLeft='다음 이자 납부일' titleSize='s' className='w-full'>
              <p className='text-black text-h2 font-semibold'>2025년 3월 5일</p>
            </Card>
            <Card titleLeft='이번주에 낼 이자' titleSize='s' className='w-full'>
              <Lufin size='l' count={1600} />
            </Card>
            <Card titleLeft='만기까지' titleSize='s' className='w-full'>
              <p className='text-black text-h2 font-semibold'>
                28 <span className='text-p1 font-regular'>일</span>
              </p>
            </Card>
          </div>

          <Card
            titleLeft='대출 상품'
            titleRight={
              <p className='font-medium text-c1 text-grey'>
                *신용등급에 따라 대출 한도와 이자율이 다를 수 있어요.
              </p>
            }
            className='w-full h-fit flex flex-col'
          >
            <div className='flex flex-row gap-4'>
              {sampleLoanProducts.map((product) => (
                <LoanProduct
                  key={product.loanProductId}
                  {...product}
                  onClick={() => setSelectedLoanProduct(product)}
                />
              ))}
            </div>
          </Card>

          <Card titleLeft='대출 내역' className='min-h-0 h-full'>
            <LoanApplicationList
              loanApplications={sampleLoanApplications}
              showMemberName={false}
              onRowClick={(loanApplicationId: number) => setSelectedLoanId(loanApplicationId)} // 타입 명확화
            />
          </Card>
        </div>
      </SidebarLayout>
    </div>
  );
};

export default StudentLoan;

import Card from '../../../components/Card/Card';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { sampleLoanProducts } from '../../../types/Loan/loan';
import LoanProduct from './components/LoanProduct';

const StudentLoan = () => {
  return (
    <SidebarLayout>
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
            <LoanProduct {...product} />
          ))}
        </div>
      </Card>
    </SidebarLayout>
  );
};

export default StudentLoan;

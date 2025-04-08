import Card from '../../../../../components/Card/Card';
import { LoanProductDTO, LoanProductListDTO } from '../../../../../types/Loan/loan';
import LoanProduct from './LoanProduct';

interface LoanProductListProps {
  loanProductList: LoanProductListDTO;
  onClick: (loanProduct: LoanProductDTO) => void;
}

const LoanProductList = ({ loanProductList, onClick }: LoanProductListProps) => {
  return (
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
        {loanProductList.map((loanProduct) => (
          <LoanProduct
            key={loanProduct.loanProductId}
            loanProduct={loanProduct}
            onClick={onClick}
          />
        ))}
      </div>
    </Card>
  );
};

export default LoanProductList;

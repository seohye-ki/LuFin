import { useEffect, useState } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import LoanApplicationList from './components/loanapplication/LoanApplicationList';
import ApplyLoan from './components/loanproduct/ApplyLoan';
import DetailLoanApplication from './components/loanapplication/DetailLoanApplication';
import LoanApplicationCards from './components/loanapplication/LoanApplicationCards';
import LoanProductList from './components/loanproduct/LoanProductList';

import {
  getLoanApplicationList,
  getLoanProductList,
  getLoanApplicationDetail,
} from '../../../libs/services/loan/loan.service';

import {
  LoanApplicationDetailDTO,
  LoanApplicationDTO,
  LoanApplicationListDTO,
  LoanProductDTO,
  LoanProductListDTO,
} from '../../../types/Loan/loan';

const StudentLoan = () => {
  const [loanApplicationDetail, setLoanApplicationDetail] =
    useState<LoanApplicationDetailDTO | null>(null);
  const [loanProductList, setLoanProductList] = useState<LoanProductListDTO>([]);
  const [loanApplicationList, setLoanApplicationList] = useState<LoanApplicationListDTO>([]);

  const [selectedLoanProduct, setSelectedLoanProduct] = useState<LoanProductDTO | null>(null);
  const [selectedLoanApplication, setSelectedLoanApplication] = useState<LoanApplicationDTO | null>(
    null,
  );

  useEffect(() => {
    fetchLoanProductList();
    fetchLoanApplicationList();
  }, []);

  const fetchLoanProductList = async () => {
    const products = await getLoanProductList();
    setLoanProductList(products);
  };

  const fetchLoanApplicationList = async () => {
    const applications = await getLoanApplicationList();
    setLoanApplicationList(applications);

    const openApplication = applications.find((app) => app.status === 'OPEN');
    if (openApplication) {
      try {
        const detail = await getLoanApplicationDetail(openApplication.loanApplicationId);
        setLoanApplicationDetail(detail);
      } catch (error) {
        console.error('대출 상세 정보 조회 실패:', error);
        setLoanApplicationDetail(null);
      }
    } else {
      setLoanApplicationDetail(null);
    }
  };

  const handleCloseDetailModal = async () => {
    setSelectedLoanApplication(null);
    await fetchLoanApplicationList();
  };

  const handleCloseApplyModal = async () => {
    setSelectedLoanProduct(null);
    await fetchLoanApplicationList();
  };

  return (
    <div>
      {selectedLoanProduct && (
        <ApplyLoan loanProduct={selectedLoanProduct} closeModal={handleCloseApplyModal} />
      )}

      {selectedLoanApplication && (
        <DetailLoanApplication
          loanApplication={selectedLoanApplication}
          closeModal={handleCloseDetailModal}
        />
      )}

      <SidebarLayout>
        <div className='w-full h-full flex flex-col gap-4'>
          {/* 상단 대출 카드 정보 */}
          <LoanApplicationCards loanApplicationDetail={loanApplicationDetail} />

          {/* 대출 상품 목록 */}
          <LoanProductList
            loanProductList={loanProductList}
            onClick={(loanProduct: LoanProductDTO) => setSelectedLoanProduct(loanProduct)}
          />

          {/* 대출 신청 내역 */}
          <LoanApplicationList
            loanApplicationList={loanApplicationList}
            showMemberName={false}
            onRowClick={(loanApplication: LoanApplicationDTO) =>
              setSelectedLoanApplication(loanApplication)
            }
          />
        </div>
      </SidebarLayout>
    </div>
  );
};

export default StudentLoan;

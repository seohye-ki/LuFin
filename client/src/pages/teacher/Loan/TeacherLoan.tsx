import { useEffect, useState } from 'react';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import ClassLoanInfo from './components/ClassLoanInfo';
import { LoanApplicationDTO } from '../../../types/Loan/loan';
import { getLoanApplicationList } from '../../../libs/services/loan/loan.service';
import LoanApplicationList from '../../student/Loan/components/loanapplication/LoanApplicationList';
import DetailLoanApplication from '../../student/Loan/components/loanapplication/DetailLoanApplication';

const TeacherLoan = () => {
  const [loanApplicationList, setLoanApplicationList] = useState<LoanApplicationDTO[]>([]);
  const [selectedLoanApplication, setSelectedLoanApplication] = useState<LoanApplicationDTO | null>(
    null,
  );

  const fetchLoanApplicationList = async () => {
    const res = await getLoanApplicationList();
    setLoanApplicationList(res);
  };

  useEffect(() => {
    fetchLoanApplicationList();
  }, []);

  const handleCloseModal = () => {
    setSelectedLoanApplication(null);
    fetchLoanApplicationList(); // 모달 닫힐 때 리스트 새로고침
  };

  return (
    <SidebarLayout>
      <div className='flex flex-col gap-4'>
        <ClassLoanInfo loanApplicationList={loanApplicationList} />
        <LoanApplicationList
          loanApplicationList={loanApplicationList}
          showMemberName={true}
          onRowClick={(loanApplication: LoanApplicationDTO) =>
            setSelectedLoanApplication(loanApplication)
          }
        />
      </div>

      {selectedLoanApplication && (
        <DetailLoanApplication
          loanApplication={selectedLoanApplication}
          closeModal={handleCloseModal}
        />
      )}
    </SidebarLayout>
  );
};

export default TeacherLoan;

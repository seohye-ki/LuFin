import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../../components/Card/Card';
import Button from '../../../components/Button/Button';
import TableView, { TableColumn, TableRow } from '../../../components/Frame/TableView';
import type { DashboardResponse } from '../../../libs/services/dashboard/dashboardService';
import { DashboardService } from '../../../libs/services/dashboard/dashboardService';
import { CreditService } from '../../../libs/services/credit/creditService';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { paths } from '../../../routes/paths';
import lufinCoin from '../../../assets/svgs/lufin-coin-16.svg';
import RecoveryApproveModal from './components/RecoveryApproveModal';

const TeacherDashboard = () => {
  const [dashboardData, setDashboardData] = useState<DashboardResponse['data'] | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [selectedStudent, setSelectedStudent] = useState<{
    id: number;
    name: string;
    creditGrade: string;
    cash: number;
    loan: number;
    investment: number;
  } | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setIsLoading(true);
        const response = await DashboardService.getTeacherDashboard();

        if (response.isSuccess && response.data) {
          setDashboardData(response.data);
        } else {
          console.error('대시보드 데이터를 불러오는데 실패했습니다.');
        }
      } catch (err) {
        console.error('대시보드 데이터 로딩 실패:', err);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const handleMissionReview = () => {
    navigate(paths.MISSION);
  };

  const handleLoanReview = () => {
    // 현재는 교사 대출 페이지가 없으므로 일단 미션 페이지로 이동
    navigate(paths.LOAN);
  };

  const handleRecoveryApproval = (student: typeof selectedStudent) => {
    setSelectedStudent(student);
  };

  const handleApproveRecovery = async () => {
    if (!selectedStudent) return;

    try {
      const response = await CreditService.approveRecovery(selectedStudent.id);

      if (response.isSuccess) {
        // 성공적으로 회생 승인이 되었을 때 대시보드 데이터를 새로 불러옴
        const dashboardResponse = await DashboardService.getTeacherDashboard();
        if (dashboardResponse.isSuccess && dashboardResponse.data) {
          setDashboardData(dashboardResponse.data);
        }
      }
    } catch (error) {
      console.error('회생 승인 실패:', error);
    } finally {
      setSelectedStudent(null);
    }
  };

  const getMissionStatusButton = (status: string) => {
    switch (status) {
      case '검토 필요':
        return (
          <Button onClick={handleMissionReview} color='primary' size='md'>
            검토하기
          </Button>
        );
      case '수행 중':
        return (
          <Button color='neutral' size='md' disabled>
            수행 중
          </Button>
        );
      case '수행 완료':
        return (
          <Button color='disabled' size='md' disabled>
            수행 완료
          </Button>
        );
      default:
        return null;
    }
  };

  const formatAmount = (amount: number) => {
    return (
      <div className='flex items-center gap-1'>
        <img src={lufinCoin} alt='루핀' className='w-4 h-4' />
        <span>{new Intl.NumberFormat('ko-KR').format(amount)}</span>
      </div>
    );
  };

  const columns: TableColumn[] = [
    { key: 'name', label: '이름' },
    { key: 'cash', label: '보유 금액' },
    { key: 'investment', label: '투자' },
    { key: 'loan', label: '대출' },
    { key: 'creditGrade', label: '신용등급' },
    { key: 'missionStatus', label: '미션' },
    { key: 'loanStatus', label: '대출 상태' },
    { key: 'items', label: '보유 아이템' },
  ];

  const formatTableRows = (): TableRow[] => {
    if (!dashboardData?.students) return [];

    return dashboardData.students.map((student) => ({
      name: student.name,
      cash: formatAmount(student.cash),
      investment: formatAmount(student.investment),
      loan: formatAmount(student.loan),
      creditGrade:
        student.creditGrade === 'F-' ? (
          <Button onClick={() => handleRecoveryApproval(student)} color='danger' size='md'>
            회생 승인
          </Button>
        ) : (
          student.creditGrade
        ),
      missionStatus: getMissionStatusButton(student.missionStatus),
      loanStatus:
        student.loanStatus === '검토 필요' ? (
          <Button onClick={handleLoanReview} color='primary' size='md'>
            검토하기
          </Button>
        ) : (
          student.loanStatus
        ),
      items: `${student.items}개`,
    }));
  };

  if (isLoading) return <div className='flex justify-center items-center h-full'>Loading...</div>;
  if (!dashboardData)
    return (
      <div className='flex justify-center items-center h-full'>데이터를 불러올 수 없습니다.</div>
    );

  return (
    <SidebarLayout>
      <div className='h-full flex flex-col gap-6'>
        {/* Statistics Section */}
        <div className='grid grid-cols-3 gap-4'>
          <Card titleLeft={dashboardData.statistics.deposit.label} className='bg-white'>
            <div className='flex flex-col'>
              <div className='flex items-center gap-2'>
                <img src={lufinCoin} alt='루핀' className='w-6 h-6' />
                <span className='text-h2 font-bold'>
                  {new Intl.NumberFormat('ko-KR').format(dashboardData.statistics.deposit.amount)}
                </span>
              </div>
              <span
                className={`text-p2 ${dashboardData.statistics.deposit.isPositive ? 'text-success' : 'text-danger'}`}
              >
                {dashboardData.statistics.deposit.isPositive ? '+' : '-'}
                {Math.abs(dashboardData.statistics.deposit.changeRate)}%
              </span>
            </div>
          </Card>
          <Card titleLeft={dashboardData.statistics.investment.label} className='bg-white'>
            <div className='flex flex-col'>
              <div className='flex items-center gap-2'>
                <img src={lufinCoin} alt='루핀' className='w-6 h-6' />
                <span className='text-h2 font-bold'>
                  {new Intl.NumberFormat('ko-KR').format(
                    dashboardData.statistics.investment.amount,
                  )}
                </span>
              </div>
              <span
                className={`text-p2 ${dashboardData.statistics.investment.isPositive ? 'text-success' : 'text-danger'}`}
              >
                {dashboardData.statistics.investment.isPositive ? '+' : '-'}
                {Math.abs(dashboardData.statistics.investment.changeRate)}%
              </span>
            </div>
          </Card>
          <Card titleLeft={dashboardData.statistics.loan.label} className='bg-white'>
            <div className='flex flex-col'>
              <div className='flex items-center gap-2'>
                <img src={lufinCoin} alt='루핀' className='w-6 h-6' />
                <span className='text-h2 font-bold'>
                  {new Intl.NumberFormat('ko-KR').format(dashboardData.statistics.loan.amount)}
                </span>
              </div>
              <span
                className={`text-p2 ${dashboardData.statistics.loan.isPositive ? 'text-success' : 'text-danger'}`}
              >
                {dashboardData.statistics.loan.isPositive ? '+' : '-'}
                {Math.abs(dashboardData.statistics.loan.changeRate)}%
              </span>
            </div>
          </Card>
        </div>

        {/* Students List Section */}
        <Card titleLeft='학생 목록' className='h-full bg-white'>
          <div className='max-h-[500px] overflow-auto'>
            <TableView columns={columns} rows={formatTableRows()} />
          </div>
        </Card>
        {selectedStudent && (
          <RecoveryApproveModal
            studentName={selectedStudent.name}
            creditGrade={selectedStudent.creditGrade}
            currentAsset={selectedStudent.cash}
            loanAmount={selectedStudent.loan}
            investmentAmount={selectedStudent.investment}
            onClose={() => setSelectedStudent(null)}
            onApprove={handleApproveRecovery}
          />
        )}
      </div>
    </SidebarLayout>
  );
};

export default TeacherDashboard;

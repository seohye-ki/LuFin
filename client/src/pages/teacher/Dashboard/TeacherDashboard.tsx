import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../../../components/Card/Card';
import Button from '../../../components/Button/Button';
import type { DashboardResponse } from '../../../libs/services/dashboard/dashboardService';
import { DashboardService } from '../../../libs/services/dashboard/dashboardService';
import SidebarLayout from '../../../components/Layout/SidebarLayout';
import { paths } from '../../../routes/paths';
import lufinCoin from '../../../assets/svgs/lufin-coin-16.svg';
import RecoveryApproveModal from './components/RecoveryApproveModal';
// import useClassroomStore from '../../../libs/store/classroomStore';

// fallback용 목데이터
const MOCK_DATA: DashboardResponse['data'] = {
  statistics: {
    deposit: {
      label: '예금',
      amount: 15000000,
      changeRate: 12.5,
      isPositive: true
    },
    investment: {
      label: '투자',
      amount: 8000000,
      changeRate: -5.2,
      isPositive: false
    },
    loan: {
      label: '대출',
      amount: 3000000,
      changeRate: 0,
      isPositive: true
    }
  },
  students: [
    {
      id: 1,
      name: '김민준',
      cash: 1500000,
      investment: 800000,
      loan: 200000,
      creditGrade: 'A+',
      missionStatus: '검토 필요',
      loanStatus: '검토 필요',
      items: 5
    },
    {
      id: 2,
      name: '이서연',
      cash: 2000000,
      investment: 1500000,
      loan: 0,
      creditGrade: 'A',
      missionStatus: '수행 중',
      loanStatus: '승인',
      items: 3
    },
    {
      id: 3,
      name: '박지호',
      cash: 500000,
      investment: 300000,
      loan: 1000000,
      creditGrade: 'B+',
      missionStatus: '수행 완료',
      loanStatus: '거절',
      items: 0
    },
    {
      id: 4,
      name: '최수아',
      cash: 3000000,
      investment: 0,
      loan: 500000,
      creditGrade: 'C',
      missionStatus: '검토 필요',
      loanStatus: '승인',
      items: 2
    },
    {
      id: 5,
      name: '정우진',
      cash: 800000,
      investment: 2000000,
      loan: 0,
      creditGrade: 'F-',
      missionStatus: '수행 중',
      loanStatus: '검토 필요',
      items: 7
    }
  ]
};

const TeacherDashboard = () => {
  const [dashboardData, setDashboardData] = useState<DashboardResponse['data'] | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isUsingMockData, setIsUsingMockData] = useState(false);
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
          // 학생이 한 명도 없는 경우 목데이터를 사용
          if (response.data.students && response.data.students.length === 0) {
            console.log('학생이 없어서 목데이터를 사용합니다.');
            setDashboardData(MOCK_DATA);
            setIsUsingMockData(true);
          } else {
            setDashboardData(response.data);
          }
        } else {
          console.log('API 응답이 실패하여 목데이터를 사용합니다.');
          setDashboardData(MOCK_DATA);
          setIsUsingMockData(true);
        }
      } catch (err) {
        console.error('대시보드 데이터 로딩 실패:', err);
        console.log('에러가 발생하여 목데이터를 사용합니다.');
        setDashboardData(MOCK_DATA);
        setIsUsingMockData(true);
      } finally {
        setIsLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

   const handleMissionReview = () => {
    navigate(paths.TEACHER_MISSION);
  };

  const handleLoanReview = () => {
    // 현재는 교사 대출 페이지가 없으므로 일단 미션 페이지로 이동
    navigate(paths.TEACHER_MISSION);
  };

  const handleRecoveryApproval = (student: typeof selectedStudent) => {
    setSelectedStudent(student);
  };

  const handleApproveRecovery = () => {
    // TODO: API 연동
    console.log('회생 승인:', selectedStudent?.id);
    setSelectedStudent(null);
  };

  const getMissionStatusButton = (status: string) => {
    switch (status) {
      case '검토 필요':
        return (
          <Button
            onClick={handleMissionReview}
            color="primary"
            size="md"
          >
            검토하기
          </Button>
        );
      case '수행 중':
        return (
          <Button
            color="neutral"
            size="md"
            disabled
          >
            수행 중
          </Button>
        );
      case '수행 완료':
        return (
          <Button
            color="disabled"
            size="md"
            disabled
          >
            수행 완료
          </Button>
        );
      default:
        return null;
    }
  };

  const formatAmount = (amount: number) => {
    return (
      <div className="flex items-center gap-1">
        <img src={lufinCoin} alt="루핀" className="w-4 h-4" />
        <span>{new Intl.NumberFormat('ko-KR').format(amount)}</span>
      </div>
    );
  };

  if (isLoading) return <div className="flex justify-center items-center h-full">Loading...</div>;
  if (!dashboardData) return <div className="flex justify-center items-center h-full">데이터를 불러올 수 없습니다.</div>;

  return (
    <SidebarLayout userRole="teacher">
      <div className="flex flex-col gap-6 p-6">
        {isUsingMockData && (
          <div className="bg-warning-light p-4 rounded-md">
            <p className="text-warning-dark font-medium">실제 데이터를 불러올 수 없어 샘플 데이터를 표시하고 있습니다.</p>
          </div>
        )}
        
        {/* Statistics Section */}
        <div className="grid grid-cols-3 gap-4">
          <Card 
            titleLeft={dashboardData.statistics.deposit.label}
            className="bg-white shadow-sm"
          >
            <div className="flex flex-col">
              <div className="flex items-center gap-2">
                <img src={lufinCoin} alt="루핀" className="w-6 h-6" />
                <span className="text-h2 font-bold">{new Intl.NumberFormat('ko-KR').format(dashboardData.statistics.deposit.amount)}</span>
              </div>
              <span className={`text-p2 ${dashboardData.statistics.deposit.isPositive ? 'text-success' : 'text-danger'}`}>
                {dashboardData.statistics.deposit.isPositive ? '+' : '-'}{Math.abs(dashboardData.statistics.deposit.changeRate)}%
              </span>
            </div>
          </Card>
          <Card 
            titleLeft={dashboardData.statistics.investment.label}
            className="bg-white shadow-sm"
          >
            <div className="flex flex-col">
              <div className="flex items-center gap-2">
                <img src={lufinCoin} alt="루핀" className="w-6 h-6" />
                <span className="text-h2 font-bold">{new Intl.NumberFormat('ko-KR').format(dashboardData.statistics.investment.amount)}</span>
              </div>
              <span className={`text-p2 ${dashboardData.statistics.investment.isPositive ? 'text-success' : 'text-danger'}`}>
                {dashboardData.statistics.investment.isPositive ? '+' : '-'}{Math.abs(dashboardData.statistics.investment.changeRate)}%
              </span>
            </div>
          </Card>
          <Card 
            titleLeft={dashboardData.statistics.loan.label}
            className="bg-white shadow-sm"
          >
            <div className="flex flex-col">
              <div className="flex items-center gap-2">
                <img src={lufinCoin} alt="루핀" className="w-6 h-6" />
                <span className="text-h2 font-bold">{new Intl.NumberFormat('ko-KR').format(dashboardData.statistics.loan.amount)}</span>
              </div>
              <span className={`text-p2 ${dashboardData.statistics.loan.isPositive ? 'text-success' : 'text-danger'}`}>
                {dashboardData.statistics.loan.isPositive ? '+' : '-'}{Math.abs(dashboardData.statistics.loan.changeRate)}%
              </span>
            </div>
          </Card>
        </div>

        {/* Students List Section */}
        <Card titleLeft="학생 목록" className="bg-white shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead>
                <tr className="text-left border-b border-grey-30">
                  <th className="py-4 px-4">이름</th>
                  <th className="py-4 px-4">보유 금액</th>
                  <th className="py-4 px-4">투자</th>
                  <th className="py-4 px-4">대출</th>
                  <th className="py-4 px-4">신용등급</th>
                  <th className="py-4 px-4">미션</th>
                  <th className="py-4 px-4">대출 상태</th>
                  <th className="py-4 px-4">보유 아이템</th>
                </tr>
              </thead>
              <tbody>
                {dashboardData.students.map((student) => (
                  <tr key={student.id} className="border-b border-grey-30">
                    <td className="py-4 px-4">{student.name}</td>
                    <td className="py-4 px-4">{formatAmount(student.cash)}</td>
                    <td className="py-4 px-4">{formatAmount(student.investment)}</td>
                    <td className="py-4 px-4">{formatAmount(student.loan)}</td>
                    <td className="py-4 px-4">
                      {student.creditGrade === 'F-' ? (
                        <Button
                          onClick={() => handleRecoveryApproval(student)}
                          color="danger"
                          size="md"
                        >
                          회생 승인
                        </Button>
                      ) : (
                        student.creditGrade
                      )}
                    </td>
                    <td className="py-4 px-4">
                      {getMissionStatusButton(student.missionStatus)}
                    </td>
                    <td className="py-4 px-4">
                      {student.loanStatus === '검토 필요' ? (
                        <Button
                          onClick={handleLoanReview}
                          color="primary"
                          size="md"
                        >
                          검토하기
                        </Button>
                      ) : (
                        student.loanStatus
                      )}
                    </td>
                    <td className="py-4 px-4">{student.items}개</td>
                  </tr>
                ))}
              </tbody>
            </table>
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
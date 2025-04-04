import { useState } from 'react';

/**
 * 회원가입 단계 관리를 위한 커스텀 훅
 * - 현재 단계 상태 관리
 * - 단계 간 이동 기능
 * - 단계별 제목 관리
 */
export const useRegisterStep = () => {
  // 현재 단계 상태 관리 (1부터 시작)
  const [currentStep, setCurrentStep] = useState(1);

  // 전체 단계 수
  const totalSteps = 5;

  // 각 단계별 제목
  const stepTitles = [
    '역할 선택', // 1단계: 사용자 유형 선택
    '기본 정보', // 2단계: 이름, 이메일 입력
    '비밀번호 설정', // 3단계: 비밀번호 설정
    '계좌 설정', // 4단계: 계좌 비밀번호 설정
    '가입 완료', // 5단계: 가입 완료
  ];

  /**
   * 다음 단계로 이동
   * - 마지막 단계가 아닐 경우에만 이동
   */
  const handleNext = () => {
    if (currentStep < totalSteps) {
      setCurrentStep((prev) => prev + 1);
    }
  };

  /**
   * 이전 단계로 이동
   * - 첫 단계가 아닐 경우에만 이동
   */
  const handlePrev = () => {
    if (currentStep > 1) {
      setCurrentStep((prev) => prev - 1);
    }
  };

  /**
   * 가입 완료 단계로 이동
   * - 모든 단계가 완료된 후 호출
   */
  const goToCompletion = () => {
    setCurrentStep(5);
  };

  return {
    currentStep, // 현재 단계
    totalSteps, // 전체 단계 수
    stepTitles, // 단계별 제목
    handleNext, // 다음 단계로 이동
    handlePrev, // 이전 단계로 이동
    goToCompletion, // 가입 완료 단계로 이동
  };
};

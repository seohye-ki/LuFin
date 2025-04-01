import { useState } from 'react';

/**
 * 회원가입 폼의 상태를 정의하는 인터페이스
 */
interface FormState {
  userType: 'teacher' | 'student' | null; // 사용자 유형 (교사/학생)
  name: string; // 이름
  email: string; // 이메일
  password: string; // 비밀번호
  confirmPassword: string; // 비밀번호 확인
  accountPassword: string; // 계좌 비밀번호
  confirmAccountPassword: string; // 계좌 비밀번호 확인
}

/**
 * 폼 유효성 검사 상태를 정의하는 인터페이스
 */
interface ValidationState {
  isUserTypeSelected: boolean; // 사용자 유형 선택 여부
  name: {
    isValid: boolean; // 이름 유효성
    message: string; // 유효성 검사 메시지
  };
  email: {
    isValid: boolean; // 이메일 유효성
    message: string; // 유효성 검사 메시지
  };
  password: {
    isValid: boolean; // 비밀번호 유효성
    message: string; // 유효성 검사 메시지
  };
  accountPassword: {
    isValid: boolean; // 계좌 비밀번호 유효성
    message: string; // 유효성 검사 메시지
  };
}

/**
 * 회원가입 폼의 상태와 유효성 검사를 관리하는 커스텀 훅
 */
export function useRegisterForm() {
  // 폼 데이터 상태 관리
  const [formData, setFormData] = useState<FormState>({
    userType: null,
    name: '',
    email: '',
    password: '',
    confirmPassword: '',
    accountPassword: '',
    confirmAccountPassword: '',
  });

  // 유효성 검사 상태 관리
  const [validation, setValidation] = useState<ValidationState>({
    isUserTypeSelected: false,
    name: {
      isValid: false,
      message: '',
    },
    email: {
      isValid: false,
      message: '',
    },
    password: {
      isValid: false,
      message: '',
    },
    accountPassword: {
      isValid: false,
      message: '',
    },
  });

  /**
   * 이메일 유효성 검사
   * @param email 검사할 이메일 주소
   * @returns 유효성 검사 결과
   */
  const validateEmail = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isValid = emailRegex.test(email);
    setValidation((prev) => ({
      ...prev,
      email: {
        isValid,
        message: isValid ? '사용 가능한 이메일입니다' : '올바른 이메일 형식이 아닙니다',
      },
    }));
    return isValid;
  };

  /**
   * 비밀번호 유효성 검사
   * - 8자 이상
   * - 비밀번호와 확인 비밀번호 일치 여부
   */
  const validatePassword = () => {
    const isLengthValid = formData.password.length >= 8;
    const isMatching = formData.password === formData.confirmPassword;
    const isValid = isLengthValid && isMatching;

    setValidation((prev) => ({
      ...prev,
      password: {
        isValid,
        message: isMatching ? '비밀번호가 일치합니다' : '비밀번호가 일치하지 않습니다',
      },
    }));
    return isValid;
  };

  /**
   * 이름 유효성 검사
   * - 2자 이상
   */
  const validateName = (name: string) => {
    const isValid = name.length >= 2;
    setValidation((prev) => ({
      ...prev,
      name: {
        isValid,
        message: isValid ? '사용 가능한 이름입니다' : '이름은 2자 이상이어야 합니다',
      },
    }));
    return isValid;
  };

  /**
   * 계좌 비밀번호 유효성 검사
   * - 6자리 숫자
   * - 계좌 비밀번호와 확인 비밀번호 일치 여부
   */
  const validateAccountPassword = () => {
    const isValid =
      /^\d{6}$/.test(formData.accountPassword) &&
      formData.accountPassword === formData.confirmAccountPassword;

    let message = '';
    if (!/^\d{6}$/.test(formData.accountPassword) && formData.accountPassword) {
      message = '계좌비밀번호는 6자리 숫자여야 합니다';
    } else if (
      /^\d{6}$/.test(formData.accountPassword) &&
      formData.accountPassword !== formData.confirmAccountPassword &&
      formData.confirmAccountPassword
    ) {
      message = '계좌비밀번호가 일치하지 않습니다';
    } else if (isValid && formData.confirmAccountPassword) {
      message = '사용 가능한 계좌비밀번호입니다';
    }

    setValidation((prev) => ({
      ...prev,
      accountPassword: {
        isValid,
        message,
      },
    }));
    return isValid;
  };

  /**
   * 입력 필드 변경 핸들러
   * @param field 변경된 필드명
   */
  const handleChange = (field: keyof FormState) => (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;

    // 계좌 비밀번호는 숫자만 입력 가능
    if (field === 'accountPassword' && !/^\d*$/.test(newValue)) {
      return;
    }

    setFormData((prev) => ({ ...prev, [field]: newValue }));

    // 필드별 유효성 검사 실행
    if (field === 'email') {
      validateEmail(newValue);
    } else if (field === 'name') {
      validateName(newValue);
    } else if (field === 'password' || field === 'confirmPassword') {
      validatePassword();
    } else if (field === 'accountPassword' || field === 'confirmAccountPassword') {
      validateAccountPassword();
    }
  };

  /**
   * 입력 필드 자동완성 핸들러
   * @param field 자동완성된 필드명
   */
  const handleAutoComplete =
    (field: keyof FormState) => (e: React.FocusEvent<HTMLInputElement>) => {
      const value = e.target.value;
      if (value) {
        if (field === 'email') {
          validateEmail(value);
        } else if (field === 'name') {
          validateName(value);
        } else if (field === 'password' || field === 'confirmPassword') {
          validatePassword();
        } else if (field === 'accountPassword' || field === 'confirmAccountPassword') {
          validateAccountPassword();
        }
      }
    };

  /**
   * 사용자 유형 설정
   * @param type 사용자 유형 (교사/학생)
   */
  const setUserType = (type: 'teacher' | 'student') => {
    setFormData((prev) => ({ ...prev, userType: type }));
    setValidation((prev) => ({ ...prev, isUserTypeSelected: true }));
  };

  return {
    formData,
    validation,
    handleChange,
    handleAutoComplete,
    setUserType,
  };
}

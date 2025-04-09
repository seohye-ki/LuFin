import { useState } from 'react';
import { registerService } from '../../../../libs/services/auth/registerService';

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
    isAvailable?: boolean;
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
    userType: null, // 1단계: 사용자 유형
    name: '', // 2단계: 기본 정보
    email: '', // 2단계: 기본 정보
    password: '', // 3단계: 비밀번호
    confirmPassword: '', // 3단계: 비밀번호 확인
    accountPassword: '', // 4단계: 계좌 비밀번호
    confirmAccountPassword: '', // 4단계: 계좌 비밀번호 확인
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
   * 이메일 유효성 검사 및 중복 확인
   */
  const validateEmail = async (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isFormatValid = emailRegex.test(email);

    if (!isFormatValid) {
      setValidation((prev) => ({
        ...prev,
        email: {
          isValid: false,
          message: '올바른 이메일 형식이 아닙니다',
          isAvailable: false,
        },
      }));
      return false;
    }

    try {
      const response = await registerService.checkEmail(email);
      const isAvailable = response.isSuccess;

      setValidation((prev) => ({
        ...prev,
        email: {
          isValid: isAvailable,
          message:
            response.message ||
            (isAvailable ? '사용 가능한 이메일입니다' : '이미 가입된 이메일입니다'),
          isAvailable,
        },
      }));
      return isAvailable;
    } catch {
      setValidation((prev) => ({
        ...prev,
        email: {
          isValid: false,
          message: '이메일 확인 중 오류가 발생했습니다',
          isAvailable: false,
        },
      }));
      return false;
    }
  };

  /**
   * 이메일 형식 검사 (실시간)
   */
  const validateEmailFormat = (email: string) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const isFormatValid = emailRegex.test(email);

    setValidation((prev) => ({
      ...prev,
      email: {
        ...prev.email,
        isValid: isFormatValid,
        message: isFormatValid ? '' : '올바른 이메일 형식이 아닙니다',
      },
    }));
    return isFormatValid;
  };

  /**
   * 비밀번호 유효성 검사
   * - 8자 이상
   * - 특수문자 포함
   * - 비밀번호와 확인 비밀번호 일치 여부
   */
  const validatePassword = () => {
    const specialCharRegex = /[!@#$%^&*(),.?":{}|<>]/;
    const isLengthValid = formData.password.length >= 8;
    const hasSpecialChar = specialCharRegex.test(formData.password);
    const isMatching = formData.password === formData.confirmPassword;
    const isValid = isLengthValid && hasSpecialChar && isMatching;

    let message = '';
    if (!isLengthValid) {
      message = '비밀번호는 8자 이상이어야 합니다';
    } else if (!hasSpecialChar) {
      message = '비밀번호는 특수문자를 포함해야 합니다';
    } else if (!isMatching) {
      message = '비밀번호가 일치하지 않습니다';
    } else {
      message = '사용 가능한 비밀번호입니다';
    }

    setValidation((prev) => ({
      ...prev,
      password: {
        isValid,
        message,
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
      validateEmailFormat(newValue);
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

  /**
   * 회원가입 제출
   */
  const handleSubmit = async () => {
    try {
      const response = await registerService.register({
        email: formData.email,
        password: formData.password,
        name: formData.name,
        role: formData.userType === 'teacher' ? 'TEACHER' : 'STUDENT',
        secondaryPassword: formData.accountPassword,
        profileImage: `https://api.dicebear.com/9.x/bottts-neutral/svg?seed=${encodeURIComponent(formData.email)}`,
      });

      if (response.isSuccess) {
        return { success: true };
      } else {
        return {
          success: false,
          message: response.message || '회원가입 중 오류가 발생했습니다',
        };
      }
    } catch {
      return {
        success: false,
        message: '서버와의 통신 중 오류가 발생했습니다',
      };
    }
  };

  return {
    formData,
    validation,
    handleChange,
    handleAutoComplete,
    setUserType,
    handleSubmit,
    validateEmail,
  };
}

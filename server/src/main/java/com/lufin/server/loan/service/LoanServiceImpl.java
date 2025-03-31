package com.lufin.server.loan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;
import com.lufin.server.loan.domain.LoanProduct;
import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanApplicationResponseDto;
import com.lufin.server.loan.repository.LoanApplicationRepository;
import com.lufin.server.loan.repository.LoanProductRepository;
import com.lufin.server.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LoanServiceImpl implements LoanService {
	private final LoanApplicationRepository loanApplicationRepository;
	private final LoanProductRepository loanProductRepository;
	private final ClassroomRepository classroomRepository;

	private Integer convertRatingToRank(Integer rating) {
		if (rating >= 85) {
			return 0;
		} else if (rating >= 70) {
			return 1;
		} else if (rating >= 55) {
			return 2;
		} else if (rating >= 40) {
			return 3;
		} else {
			return 4;
		}
	}

	@Override
	@Transactional
	public LoanApplicationResponseDto createLoanApplication(LoanApplicationRequestDto request, Member member,
		Integer classId) {
		Classroom classroom = classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));

		// 진행중인 대출 여부 확인
		List<LoanApplicationStatus> activeStatuses = List.of(
			LoanApplicationStatus.PENDING,
			LoanApplicationStatus.APPROVED,
			LoanApplicationStatus.OPEN
		);
		if (loanApplicationRepository.existsByMemberAndClassroomAndStatusIn(member, classroom, activeStatuses)) {
			throw new BusinessException(ErrorCode.LOAN_APPLICATION_ALREADY_EXISTS);
		}

		// 대출 상품 존재 여부 확인
		LoanProduct loanProduct = loanProductRepository.findById(request.loanProductId())
			.orElseThrow(() -> new BusinessException(ErrorCode.LOAN_PRODUCT_NOT_FOUND));

		// F등급 확인
		Integer rank = convertRatingToRank(member.getStatus().getCreditRating());
		if (rank == 4)
			throw new BusinessException(ErrorCode.INSUFFICIENT_CREDIT_SCORE);

		// 대출 상품 등급과 회원 등급 일치 확인
		if (!loanProduct.getCreditRank().equals(rank)) {
			throw new BusinessException(ErrorCode.INSUFFICIENT_CREDIT_SCORE);
		}

		// 대출 한도 초과 여부 확인
		if (request.requestedAmount() > loanProduct.getMaxAmount()) {
			throw new BusinessException(ErrorCode.LOAN_AMOUNT_EXCEEDS_MAX);
		}

		// 이자액 계산
		BigDecimal interestRate = loanProduct.getInterestRate();
		BigDecimal requestedAmount = BigDecimal.valueOf(request.requestedAmount());
		int interestAmount = requestedAmount.multiply(interestRate).setScale(0, RoundingMode.HALF_UP).intValue();

		LoanApplication application = LoanApplication.create(
			member,
			classroom,
			loanProduct,
			request.description(),
			request.requestedAmount(),
			interestAmount);
		loanApplicationRepository.save(application);

		return LoanApplicationResponseDto.from(application);
	}
}

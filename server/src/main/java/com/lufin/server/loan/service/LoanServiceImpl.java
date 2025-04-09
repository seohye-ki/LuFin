package com.lufin.server.loan.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.domain.CreditScore;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.domain.LoanApplicationStatus;
import com.lufin.server.loan.domain.LoanProduct;
import com.lufin.server.loan.dto.LoanApplicationApprovalDto;
import com.lufin.server.loan.dto.LoanApplicationDetailDto;
import com.lufin.server.loan.dto.LoanApplicationListDto;
import com.lufin.server.loan.dto.LoanApplicationRequestDto;
import com.lufin.server.loan.dto.LoanProductResponseDto;
import com.lufin.server.loan.repository.LoanApplicationRepository;
import com.lufin.server.loan.repository.LoanProductRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.service.TransactionHistoryService;

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
	private final CreditScoreRepository creditScoreRepository;
	private final AccountRepository accountRepository;
	private final TransactionHistoryService transactionHistoryService;

	private Integer convertRatingToRank(Integer rating) {
		log.info("🔧[신용 등급 변환] - rating: {}", rating);
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

	private Account getActiveAccount(Member member) {
		return accountRepository.findOpenAccountByMemberIdWithPessimisticLock(member.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private Account getClassAccount(Integer classId) {
		return accountRepository.findByClassroomId(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	@Override
	public List<LoanProductResponseDto> getLoanProducts(Member member) {
		CreditScore creditScore = creditScoreRepository.findById(member.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.CREDIT_SCORE_NOT_FOUND));
		Integer rank = convertRatingToRank(Integer.valueOf(creditScore.getScore()));
		log.info("🔍[대출 상품 조회]");
		List<LoanProduct> result = loanProductRepository.findByCreditRank(rank);
		log.info("✅[대출 상품 조회 성공] - count: {}", result.size());
		return result.stream()
			.map(LoanProductResponseDto::from)
			.toList();
	}

	@Override
	@Transactional
	public LoanApplicationDetailDto createLoanApplication(LoanApplicationRequestDto request, Member member,
		Integer classId) {
		log.info("📝[대출 신청 요청] - memberId: {}, classId: {}, amount: {}", member.getId(), classId,
			request.requestedAmount());
		Classroom classroom = classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));

		List<LoanApplicationStatus> activeStatuses = List.of(
			LoanApplicationStatus.PENDING,
			LoanApplicationStatus.APPROVED,
			LoanApplicationStatus.OPEN
		);
		if (loanApplicationRepository.existsByMemberAndClassroomAndStatusIn(member, classroom, activeStatuses)) {
			log.warn("🚫[중복 대출 신청] - memberId: {}, classId: {}", member.getId(), classId);
			throw new BusinessException(ErrorCode.LOAN_APPLICATION_ALREADY_EXISTS);
		}

		LoanProduct loanProduct = loanProductRepository.findById(request.loanProductId())
			.orElseThrow(() -> new BusinessException(ErrorCode.LOAN_PRODUCT_NOT_FOUND));

		CreditScore creditScore = creditScoreRepository.findById(member.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.CREDIT_SCORE_NOT_FOUND));
		log.info("✅[신용 점수 조회 성공] - memberId: {}, score: {}", member.getId(), creditScore.getScore());
		Integer rank = convertRatingToRank(Integer.valueOf(creditScore.getScore()));
		if (rank == 4) {
			log.warn("🚫[신용 등급 제한] - memberId: {}, rank: {}", member.getId(), rank);
			throw new BusinessException(ErrorCode.INSUFFICIENT_CREDIT_SCORE);
		}

		if (!loanProduct.getCreditRank().equals(rank)) {
			log.warn("🚫[대출 상품 등급 불일치] - memberId: {}, productRank: {}, memberRank: {}", member.getId(),
				loanProduct.getCreditRank(), rank);
			throw new BusinessException(ErrorCode.INSUFFICIENT_CREDIT_SCORE);
		}

		if (request.requestedAmount() > loanProduct.getMaxAmount()) {
			log.warn("🚫[대출 한도 초과] - memberId: {}, requested: {}, max: {}", member.getId(), request.requestedAmount(),
				loanProduct.getMaxAmount());
			throw new BusinessException(ErrorCode.LOAN_AMOUNT_EXCEEDS_MAX);
		}

		BigDecimal interestRate = loanProduct.getInterestRate();
		BigDecimal requestedAmount = BigDecimal.valueOf(request.requestedAmount());
		int interestAmount = requestedAmount.multiply(interestRate).setScale(0, RoundingMode.FLOOR).intValue();
		double totalInstallments = loanProduct.getPeriod() / 7.0;
		int installmentInterestAmount = (int)Math.floor(interestAmount / totalInstallments);
		LoanApplication application = LoanApplication.create(
			member,
			classroom,
			loanProduct,
			request.description(),
			request.requestedAmount(),
			installmentInterestAmount);
		loanApplicationRepository.save(application);
		log.info("✅[대출 신청 완료] - applicationId: {}, memberId: {}", application.getId(), member.getId());
		return LoanApplicationDetailDto.from(application);
	}

	@Override
	public List<LoanApplicationListDto> getLoanApplications(Member member, Integer classId) {
		log.info("🔍[대출 신청 내역 조회] - memberId: {}, classId: {}", member.getId(), classId);
		Classroom classroom = classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));
		List<LoanApplication> applications;
		if (member.getMemberRole() == MemberRole.TEACHER) {
			applications = loanApplicationRepository.findByClassroom(classroom);
		} else {
			applications = loanApplicationRepository.findByMemberAndClassroom(member, classroom);
		}
		log.info("✅[대출 신청 내역 조회 완료] - count: {}", applications.size());
		return applications.stream()
			.map(LoanApplicationListDto::from)
			.toList();
	}

	@Override
	public LoanApplicationDetailDto getLoanApplicationDetail(Integer loanApplicationId, Member member,
		Integer classId) {
		log.info("🔍[대출 신청 상세 조회] - loanApplicationId: {}, memberId: {}, classId: {}", loanApplicationId, member.getId(),
			classId);
		LoanApplication application = loanApplicationRepository.findById(loanApplicationId)
			.orElseThrow(() -> new BusinessException(ErrorCode.LOAN_APPLICATION_NOT_FOUND));
		classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));

		if (member.getMemberRole() == MemberRole.TEACHER) {
			if (!application.getClassroom().getId().equals(classId)) {
				log.warn("🚫[대출 신청 조회 오류] - 요청 반과 일치하지 않음");
				throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST);
			}
		} else {
			if (!application.getMember().getId().equals(member.getId())) {
				log.warn("🚫[대출 신청 조회 오류 - 학생] - 대출 신청자가 아님. applicationMemberId: {}, requestMemberId: {}",
					application.getMember().getId(), member.getId());
				throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST);
			}
			if (!application.getClassroom().getId().equals(classId)) {
				log.warn("🚫[대출 신청 조회 오류 - 학생] - 요청 반과 일치하지 않음. applicationClassId: {}, requestClassId: {}",
					application.getClassroom().getId(), classId);
				throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST);
			}
		}
		log.info("✅[대출 신청 상세 조회 완료] - loanApplicationId: {}", loanApplicationId);
		return LoanApplicationDetailDto.from(application);
	}

	@Override
	@Transactional
	public LoanApplicationDetailDto approveOrRejectLoanApplication(LoanApplicationApprovalDto requestDto,
		Integer loanApplicationId, Member teacher, Integer classId) {
		log.info("📝[대출 신청 승인/거절] - loanApplicationId: {}, memberId: {}, classId: {}", loanApplicationId,
			teacher.getId(),
			classId);
		LoanApplication application = loanApplicationRepository.findById(loanApplicationId)
			.orElseThrow(() -> new BusinessException(ErrorCode.LOAN_APPLICATION_NOT_FOUND));
		classroomRepository.findById(classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));
		if (!application.getClassroom().getId().equals(classId)) {
			log.warn("🚫[대출 신청 승인 거절 오류] - 요청 반과 일치하지 않음. applicationClassId: {}, requestClassId: {}",
				application.getClassroom().getId(), classId);
			throw new BusinessException(ErrorCode.FORBIDDEN_REQUEST);
		}

		if (requestDto.status() == LoanApplicationStatus.APPROVED) {
			application.open();
			Account account = getActiveAccount(application.getMember());
			account.deposit(application.getRequiredAmount());
			accountRepository.save(account);
			Account classAccount = getClassAccount(classId);
			log.info("💰[대출 금액 입금 완료] - memberId: {}, amount: {}, balance: {}", application.getMember().getId(),
				application.getRequiredAmount(), account.getBalance());
			transactionHistoryService.record(
				classAccount,
				account.getAccountNumber(),
				application.getMember(),
				application.getRequiredAmount(),
				account.getBalance(),
				TransactionType.DEPOSIT,
				HistoryStatus.SUCCESS,
				"대출 실행",
				TransactionSourceType.LOAN_DISBURSEMENT
			);
			log.info("✅[대출 승인 완료] - applicationId: {}", application.getId());
		} else if (requestDto.status() == LoanApplicationStatus.REJECTED) {
			application.reject();
			log.info("❌[대출 거절 완료] - applicationId: {}", application.getId());
		} else {
			throw new BusinessException(ErrorCode.INVALID_ENUM);
		}
		loanApplicationRepository.save(application);
		return LoanApplicationDetailDto.from(application);
	}
}

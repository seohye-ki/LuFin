package com.lufin.server.loan.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.loan.domain.LoanApplication;
import com.lufin.server.loan.repository.LoanApplicationRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanPaymentServiceImpl implements LoanPaymentService {

	private final LoanApplicationRepository loanApplicationRepository;
	private final AccountRepository accountRepository;
	private final TransactionHistoryService transactionHistoryService;
	private final CreditScoreService creditScoreService;

	private Account getActiveAccount(Member member, int classId) {
		return accountRepository.findOpenAccountByMemberIdWithPessimisticLock(member.getId(), classId)
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	private Account getClassAccount(Classroom classroom) {
		return accountRepository.findByClassroomId(classroom.getId())
			.orElseThrow(() -> new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND));
	}

	@Override
	@Transactional
	public void processInterestPayments() {
		log.info("이자 납부 처리 시작");
		List<LoanApplication> loans = loanApplicationRepository.findLoansWithNextPaymentDateToday();
		log.info("오늘 이자 납부 예정 대출 건수: {}", loans.size());

		for (LoanApplication loan : loans) {
			int classId = loan.getClassroom().getId();
			if (isDueDateToday(loan)) {
				log.info("대출 [{}]의 대출 만기일입니다.", loan.getId());
				loan.resetOverdueCount();
				if (attemptInterestPayment(loan, true)) {
					log.info("대출 [{}]의 이자 납부 성공 (만기일)", loan.getId());
					creditScoreService.applyScoreChange(loan.getMember(), 1, CreditEventType.LOAN_INTEREST_REPAYMENT,
						classId);
				} else {
					log.warn("⚠️ 대출 [{}]의 이자 납부 실패 (만기일)", loan.getId());
					creditScoreService.applyScoreChange(loan.getMember(), -4 + (-2 * loan.getOverdueCount()),
						CreditEventType.LOAN_INTEREST_DEFAULT, classId);
				}
			} else {
				log.info("대출 [{}]의 대출 만기일이 아닙니다.", loan.getId());
				if (attemptInterestPayment(loan, false)) {
					log.info("대출 [{}]의 이자 납부 성공 (일반일)", loan.getId());
					creditScoreService.applyScoreChange(loan.getMember(), 1, CreditEventType.LOAN_INTEREST_REPAYMENT,
						classId);
					loan.resetOverdueCount();
				} else {
					log.warn("⚠️ 대출 [{}]의 이자 납부 실패 (일반일)", loan.getId());
					creditScoreService.applyScoreChange(loan.getMember(), -4 + (-2 * loan.getOverdueCount()),
						CreditEventType.LOAN_INTEREST_DEFAULT, classId);
					loan.incrementOverdueCount();
				}
				loan.updateNextPaymentDate();
				loanApplicationRepository.save(loan);
			}
		}
		log.info("이자 납부 처리 완료");
	}

	private boolean isDueDateToday(LoanApplication loan) {
		return LocalDateTime.now().toLocalDate().equals(loan.getDueDate().toLocalDate());
	}

	private boolean attemptInterestPayment(LoanApplication loan, boolean isDueDate) {
		int interestAmount = loan.calculateNextInterestAmount();
		log.info("대출 [{}]의 이자 금액 계산: {}", loan.getId(), interestAmount);

		Account account = getActiveAccount(loan.getMember(), loan.getClassroom().getId());
		Account classAccount = getClassAccount(loan.getClassroom());
		if (!isDueDate && account.getBalance() < interestAmount) {
			log.warn("⚠️(일반일) 잔액 부족으로 이자 납부 실패 (잔액: {}, 이자: {})", account.getBalance(), interestAmount);
			return false;
		}
		account.forceWithdraw(interestAmount);
		accountRepository.save(account);
		classAccount.deposit(interestAmount);
		accountRepository.save(classAccount);
		transactionHistoryService.record(
			account,
			classAccount.getAccountNumber(),
			loan.getMember(),
			interestAmount,
			account.getBalance(),
			TransactionType.WITHDRAWAL,
			HistoryStatus.SUCCESS,
			"이자 납부",
			TransactionSourceType.LOAN_INTEREST_REPAYMENT);
		return account.getBalance() >= 0;
	}

	@Override
	@Transactional
	public void processPrincipalRepayments() {
		log.info("원금 상환 처리 시작");
		List<LoanApplication> loans = loanApplicationRepository.findLoansWithDueDateToday();
		log.info("오늘 원금 상환 예정 대출 건수: {}", loans.size());
		for (LoanApplication loan : loans) {
			if (attemptPrincipalRepayment(loan)) {
				log.info("대출 [{}]의 원금 상환 성공", loan.getId());
				handleSuccessPrincipalRepayment(loan);
			} else {
				log.warn("⚠️ 대출 [{}]의 원금 상환 실패", loan.getId());
				handleFailPrincipalRepayment(loan);
			}
		}
		log.info("원금 상환 처리 완료");
	}

	private boolean attemptPrincipalRepayment(LoanApplication loan) {
		Account account = getActiveAccount(loan.getMember(), loan.getClassroom().getId());
		Account classAccount = getClassAccount(loan.getClassroom());
		account.forceWithdraw(loan.getRequiredAmount());
		accountRepository.save(account);
		log.info("대출 [{}]의 계좌에서 원금 금액 [{}] 출금 완료", loan.getId(), loan.getRequiredAmount());
		classAccount.deposit(loan.getRequiredAmount());
		accountRepository.save(classAccount);
		transactionHistoryService.record(
			account,
			classAccount.getAccountNumber(),
			loan.getMember(),
			loan.getRequiredAmount(),
			account.getBalance(),
			TransactionType.WITHDRAWAL,
			HistoryStatus.SUCCESS,
			"원금 상환",
			TransactionSourceType.LOAN_PRINCIPAL_REPAYMENT);
		log.info("대출 [{}]의 원금 상환 기록 완료 - 잔액: {}", loan.getId(), account.getBalance());
		return account.getBalance() >= 0;
	}

	private void handleSuccessPrincipalRepayment(LoanApplication loan) {
		creditScoreService.applyScoreChange(loan.getMember(), 3, CreditEventType.LOAN_PRINCIPLE_REPAYMENT,
			loan.getClassroom().getId());
		loan.close();
		loanApplicationRepository.save(loan);
	}

	private void handleFailPrincipalRepayment(LoanApplication loan) {
		creditScoreService.applyScoreChange(loan.getMember(), -10, CreditEventType.LOAN_PRINCIPLE_DEFAULT,
			loan.getClassroom().getId());
		loan.overdue();
		loanApplicationRepository.save(loan);
	}
}

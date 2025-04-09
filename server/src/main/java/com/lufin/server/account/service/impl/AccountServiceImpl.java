package com.lufin.server.account.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.domain.AccountType;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.account.service.AccountService;
import com.lufin.server.account.util.AccountNumberGenerator;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;
import com.lufin.server.transaction.domain.TransactionHistory;
import com.lufin.server.transaction.repository.TransactionHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;
	private final TransactionHistoryRepository transactionHistoryRepository;

	@Transactional
	@Override
	public Account createAccountForMember(int memberId) {
		log.info("[계좌 생성] member {}", memberId);
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> {
				log.warn("🏦[회원 정보 없음] memberId: {}", memberId);
				return new BusinessException(MEMBER_NOT_FOUND);
			});

		String accountNumber = generateUniqueAccountNumber();

		Account account = Account.create(accountNumber, member);
		return accountRepository.save(account);
	}

	@Transactional
	@Override
	public Account createAccountForClassroom(Classroom classroom) {
		log.info("[클래스 계좌 생성] member {}", classroom.getId());
		String accountNumber = generateUniqueAccountNumber();
		Account account = Account.createClassAccount(accountNumber, classroom);
		return accountRepository.save(account);
	}

	private String generateUniqueAccountNumber() {
		log.debug("[계좌번호 생성]");
		String newAccountNumber;
		do {
			newAccountNumber = AccountNumberGenerator.generateAccountNumber();
		} while (accountRepository.existsByAccountNumber(newAccountNumber));
		return newAccountNumber;
	}

	@Override
	public int getCashBalance(int memberId, int classroomId) {
		log.info("[현금 자산 확인] member {}", memberId);
		return accountRepository.findByMemberIdAndClassroomIdAndType(memberId, classroomId, AccountType.DEPOSIT)
			.map(Account::getBalance)
			.orElse(0);
	}

	@Override
	public long getTotalClassDeposit(int classId) {
		// 클래스 계좌는 무조건 1개
		Account account = accountRepository.findByClassroomId(classId)
			.orElseThrow(() -> {
				log.warn("🏦[클래스 계좌 없음] classId={}", classId);
				return new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
			});

		long balance = account.getBalance();
		log.debug("[예금 잔액 결과] classId={}, balance={}", classId, balance);
		return balance;
	}

	@Override
	public long getTotalClassDeposit(int classId, LocalDate date) {
		log.info("[클래스 기준일 예금 잔액 조회] classId={}, 기준일={}", classId, date);

		// 클래스 계좌 1개 조회
		Account account = accountRepository.findByClassroomId(classId)
			.orElseThrow(() -> {
				log.warn("🏦[계좌 없음] classId={}", classId);
				return new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
			});

		// 기준일 이전의 마지막 거래 내역 조회
		Optional<TransactionHistory> latestBefore = transactionHistoryRepository
			.findTopByFromAccount_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
				account.getId(),
				date.atTime(23, 59, 59)
			);

		long balance = latestBefore.map(TransactionHistory::getBalanceAfter).orElse(0);
		log.debug("[기준일 잔액] accountId={}, 기준일={}, balance={}", account.getId(), date, balance);

		return balance;
	}
}

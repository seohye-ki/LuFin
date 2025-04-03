package com.lufin.server.account.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.domain.AccountType;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.account.service.AccountService;
import com.lufin.server.account.util.AccountNumberGenerator;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final MemberRepository memberRepository;

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
	public int getCashBalance(int memberId) {
		log.info("[현금 자산 확인] member {}", memberId);
		return accountRepository.findByMemberIdAndType(memberId, AccountType.DEPOSIT)
			.map(Account::getBalance)
			.orElse(0);
	}

	@Override
	public int getTotalAsset(int memberId) {
		return getCashBalance(memberId);
	}
}

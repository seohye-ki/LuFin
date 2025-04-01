package com.lufin.server.account.service.impl;

import static com.lufin.server.common.constants.ErrorCode.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.account.service.AccountService;
import com.lufin.server.account.util.AccountNumberGenerator;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	final AccountRepository accountRepository;
	final MemberRepository memberRepository;

	@Transactional
	@Override
	public Account createAccountForMember(int memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

		String accountNumber = generateUniqueAccountNumber();

		Account account = Account.create(accountNumber, member);
		return accountRepository.save(account);
	}

	@Transactional
	@Override
	public Account createAccountForClassroom(Classroom classroom) {
		String accountNumber = generateUniqueAccountNumber();
		Account account = Account.createClassAccount(accountNumber, classroom);
		return accountRepository.save(account);
	}

	private String generateUniqueAccountNumber() {
		String newAccountNumber;
		do {
			newAccountNumber = AccountNumberGenerator.generateAccountNumber();
		} while (accountRepository.existsByAccountNumber(newAccountNumber));
		return newAccountNumber;
	}
}

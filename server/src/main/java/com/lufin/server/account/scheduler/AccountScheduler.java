package com.lufin.server.account.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountScheduler {

	private final AccountRepository accountRepository;

	@Scheduled(cron = "0 0 0 * * *") // 매일 00:00
	@Transactional
	public void closeOldClassAccounts() {
		LocalDateTime limit = LocalDateTime.now().minusYears(1);
		List<Account> accounts = accountRepository.findClassAccountsBefore(limit);

		for (Account account : accounts) {
			if (!account.isClosed()) {
				account.close();
				log.info("[자동 해지 완료] classAccountId: {}, createdAt: {}", account.getId(), account.getCreatedAt());
			}
		}
	}
}

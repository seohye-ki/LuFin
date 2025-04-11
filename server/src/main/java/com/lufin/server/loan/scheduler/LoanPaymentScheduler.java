package com.lufin.server.loan.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lufin.server.loan.service.LoanPaymentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoanPaymentScheduler {

	private final LoanPaymentService loanPaymentService;

	@Scheduled(cron = "0 0 * * * ?")
	public void executeLoanPayment() {
		log.info("🕛 이자 납부 스케줄러 실행");
		loanPaymentService.processInterestPayments();
		log.info("🕛 원금 상환 스케줄러 실행");
		loanPaymentService.processPrincipalRepayments();
	}
}

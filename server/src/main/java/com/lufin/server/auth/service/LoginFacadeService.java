package com.lufin.server.auth.service;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.loan.service.LoanDashboardService;
import com.lufin.server.stock.service.StockService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginFacadeService {

	private final AccountService accountService;
	private final StockService stockService;
	private final LoanDashboardService loanService;

	public int getTotalAsset(int memberId, int classId) {
		if (classId == 0) {
			return 0;
		}
		int cash = accountService.getCashBalance(memberId, classId);
		int stock = stockService.getTotalValuation(memberId, classId);
		int loan = loanService.getLoanPrincipal(memberId, classId);
		return cash + stock + loan;
	}
}

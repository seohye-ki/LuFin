package com.lufin.server.loan.service;

public interface LoanPaymentService {

	void processInterestPayments();

	void processPrincipalRepayments();
}

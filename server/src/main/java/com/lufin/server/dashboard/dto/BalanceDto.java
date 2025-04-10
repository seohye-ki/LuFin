package com.lufin.server.dashboard.dto;

public class BalanceDto {
	public record balanceDto(Integer balance) {
		public balanceDto(Integer balance) {
			this.balance = balance;
		}
	}
}

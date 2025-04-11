package com.lufin.server.loan.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "loan_products")
@Getter
public class LoanProduct {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_product_id")
	private Integer id;

	@Column(name = "name", length = 30, nullable = false)
	private String name;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "credit_rank", nullable = false)
	private Integer creditRank;

	@Column(name = "max_amount", nullable = false)
	private Integer maxAmount;

	@Column(name = "interest_rate", nullable = false)
	private BigDecimal interestRate;

	@Column(name = "period", nullable = false)
	private Integer period;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	public static LoanProduct create(String name, String description, Integer creditRank,
		Integer maxAmount, BigDecimal interestRate, Integer period) {
		LoanProduct loanProduct = new LoanProduct();
		loanProduct.name = name;
		loanProduct.description = description;
		loanProduct.creditRank = creditRank;
		loanProduct.maxAmount = maxAmount;
		loanProduct.interestRate = interestRate;
		loanProduct.period = period;
		loanProduct.createdAt = LocalDateTime.now();
		loanProduct.updatedAt = LocalDateTime.now();
		return loanProduct;
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}
}
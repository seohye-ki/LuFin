package com.lufin.server.loan.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.member.domain.Member;

@Entity
@Table(name = "loan_applications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LoanApplication {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "loan_application_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroom_id", nullable = false)
	private Classroom classroom;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "loan_product_id", nullable = false)
	private LoanProduct loanProduct;

	@Column(name = "description", nullable = false)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private LoanApplicationStatus status = LoanApplicationStatus.PENDING;

	@Column(name = "required_amount", nullable = false)
	private Integer requiredAmount;

	@Column(name = "interest_amount", nullable = false)
	private Integer interestAmount;

	@Column(name = "overdue_count")
	private Integer overdueCount;

	@Column(name = "next_payment_date")
	private LocalDateTime nextPaymentDate;

	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "started_at")
	private LocalDateTime startedAt;

	@Column(name = "due_date")
	private LocalDateTime dueDate;

	public static LoanApplication create(
		Member member,
		Classroom classroom,
		LoanProduct loanProduct,
		String description,
		Integer requiredAmount,
		Integer interestAmount
	) {
		LoanApplication application = new LoanApplication();
		application.member = member;
		application.classroom = classroom;
		application.loanProduct = loanProduct;
		application.description = description;
		application.status = LoanApplicationStatus.PENDING;
		application.requiredAmount = requiredAmount;
		application.interestAmount = interestAmount;
		application.overdueCount = 0;
		application.createdAt = LocalDateTime.now();
		return application;
	}

	public void approve() {
		this.status = LoanApplicationStatus.APPROVED;
	}

	public void reject() {
		this.status = LoanApplicationStatus.REJECTED;
	}

	public void open() {
		this.status = LoanApplicationStatus.OPEN;
		this.startedAt = LocalDateTime.now();
		this.nextPaymentDate = this.startedAt.plusDays(7);
		this.dueDate = this.startedAt.plusDays(this.loanProduct.getPeriod());
	}

	public void overdued() {
		this.status = LoanApplicationStatus.OVERDUED;
	}

	public void close() {
		this.status = LoanApplicationStatus.CLOSED;
	}

	// 이자 납부액 계산
	public Integer calculateNextInterestAmount() {
		int overdueInterest = interestAmount * overdueCount;
		return interestAmount + overdueInterest;
	}
}

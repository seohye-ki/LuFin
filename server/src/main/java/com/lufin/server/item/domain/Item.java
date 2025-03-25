package com.lufin.server.item.domain;

import java.time.LocalDateTime;

import com.lufin.server.classroom.domain.Classroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "item_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_id", nullable = false)
	private Classroom classroom;

	@Column(name = "name", nullable = false, length = 50)
	private String name;

	@Column(name = "type", nullable = false)
	private Integer type = 1; // 0: 즉시 사용 가능, 1: 사용 요청 필요

	@Column(name = "price", nullable = false)
	private Integer price = 0;

	@Column(name = "quantity_available", nullable = false)
	private Integer quantityAvailable = 0;

	@Column(name = "quantity_sold", nullable = false)
	private Integer quantitySold = 0;

	@Column(name = "status", nullable = false)
	private Boolean status = true; // 판매 상태 (true: 판매 중)

	@Column(name = "expiration_date", nullable = false)
	private LocalDateTime expirationDate;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	protected void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void modifyInfo(String name, Integer price, Integer quantityAvailable, LocalDateTime expirationDate) {
		this.name = name;
		this.price = price;
		this.quantityAvailable = quantityAvailable;
		this.expirationDate = expirationDate;
	}

	public void disable() {
		this.status = false;
	}

	public void enable() {
		this.status = true;
	}


}
package com.lufin.server.item.domain;

import java.time.LocalDateTime;

import javax.net.ssl.SSLSession;

import com.lufin.server.classroom.domain.Classroom;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
	@JoinColumn(name = "classroom_id", nullable = false)
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

	public static Item create(Classroom classroom, String name, Integer type, Integer price, int quantity, LocalDateTime expirationDate) {
		Item item = new Item();
		item.classroom = classroom;
		item.name = name;
		item.type = type;
		item.price = price;
		item.quantityAvailable = quantity;
		item.quantitySold = 0;
		item.status = true;
		item.expirationDate = expirationDate;
		item.createdAt = LocalDateTime.now();
		item.updatedAt = LocalDateTime.now();
		return item;
	}

	public void changeName(String name) {
		this.name = name;
	}

	public void changePrice(Integer price) {
		this.price = price;
	}

	public void changeQuantityAvailable(Integer quantityAvailable) {
		this.quantityAvailable = quantityAvailable;
	}

	public void changeExpirationDate(LocalDateTime expirationDate) {
		this.expirationDate = expirationDate;
	}

	public void increaseQuantitySold() {
		this.quantitySold++;
	}

	public void decreaseQuantitySold() {
		this.quantitySold--;
	}

	public void disable() {
		this.status = false;
	}

	public void enable() {
		this.status = true;
	}

	@PreUpdate
	protected void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}
}

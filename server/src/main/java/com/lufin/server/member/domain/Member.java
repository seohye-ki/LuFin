package com.lufin.server.member.domain;

import static com.lufin.server.member.util.MemberValidator.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.stock.domain.StockPortfolio;
import com.lufin.server.stock.domain.StockTransactionHistories;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	/* 양방향 연관관계 */
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MissionParticipation> missionParticipations = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockPortfolio> stockPortfolios = new ArrayList<>();

	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<StockTransactionHistories> stockTransactionHistories = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Integer id;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_role", nullable = false)
	private MemberRole memberRole;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "profile_image")
	private String profileImage;

	@Column(name = "name", nullable = false, length = 10)
	private String name;

	@Column(name = "certification_number")
	private String certificationNumber;

	@Embedded
	private MemberAuth auth;

	@Embedded
	private MemberStatus status;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public static Member createStudent(String email, String name, String password, String secondaryPassword) {
		return create(email, name, password, secondaryPassword, MemberRole.STUDENT);
	}

	public static Member createTeacher(String email, String name, String password, String secondaryPassword) {
		return create(email, name, password, secondaryPassword, MemberRole.TEACHER);
	}

	private static Member create(String email, String name, String password, String secondaryPassword,
		MemberRole role) {
		isValidEmail(email);
		Member member = new Member();
		member.email = email;
		member.name = name;
		member.memberRole = role;
		member.auth = new MemberAuth(password, secondaryPassword);
		member.status = new MemberStatus();
		return member;
	}

	@PrePersist
	private void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void updateLastLogin() {
		this.auth.updateLastLogin();
	}

	/**
	 * MissionParticipation과의 양방향 연관관계 일관성 유지를 위한 메서드
	 * @param participation
	 */
	public void addMissionParticipation(MissionParticipation participation) {
		if (!this.missionParticipations.contains(participation)) {
			this.missionParticipations.add(participation);
		}

		// 이미 참여에 회원이 설정되어 있지 않은 경우에만 설정 (무한루프 방지)
		if (participation.getMember() != this) {
			participation.setMember(this);
		}

	}

	/**
	 * MissionParticipation과의 양방향 연관관계 일관성 유지를 위한 메서드
	 * @param participation
	 */
	public void removeMissionParticipation(MissionParticipation participation) {
		if (participation != null) {
			this.missionParticipations.remove(participation);
			participation.setMember(null);
		}
	}

	/**
	 * Stockportfolio와의 양방향 연관관계 일관성 유지를 위한 메서드
	 * @param portfolio
	 */
	public void addStockPortfolio(StockPortfolio portfolio) {
		if (!this.stockPortfolios.contains(portfolio)) {
			this.stockPortfolios.add(portfolio);
		}

		// 양방향 관계 설정(무한 루프 방지)
		if (portfolio.getMember() != this) {
			portfolio.setMember(this);
		}
	}

	/**
	 * Stockportfolio와의 양방향 연관관계 일관성 유지를 위한 메서드
	 * @param portfolio
	 */
	public void removeStockPortfolio(StockPortfolio portfolio) {
		this.stockPortfolios.remove(portfolio);

		if (portfolio.getMember() == this) {
			portfolio.setMember(null);
		}
	}

	/**
	 * StockTransactionHistories와의 양방향 연관관계 일관성 유지를 위한 메서드
	 * @param histories
	 */
	public void addStockTransactionHistories(StockTransactionHistories histories) {
		if (!this.stockTransactionHistories.contains(histories)) {
			this.stockTransactionHistories.add(histories);
		}

		// 양방향 관계 설정(무한 루프 방지)
		if (histories.getMember() != this) {
			histories.setMember(this);
		}
	}

	public void removeStockTransactionHistories(StockTransactionHistories histories) {
		if (this.stockTransactionHistories.contains(histories) && histories.getMember() == this) {
			this.stockTransactionHistories.remove(histories);
		}

		if (histories.getMember() == this) {
			histories.setMember(null);
		}
	}
}

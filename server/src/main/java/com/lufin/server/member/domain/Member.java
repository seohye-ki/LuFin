package com.lufin.server.member.domain;

import static com.lufin.server.member.util.MemberValidator.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lufin.server.mission.domain.MissionParticipation;

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

	@Column(name = "activation_status", nullable = false)
	private byte activationStatus;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public static Member createStudent(String email, String name, String password, String secondaryPassword,
		String profileImage) {
		return create(email, name, password, secondaryPassword, MemberRole.STUDENT, profileImage);
	}

	public static Member createTeacher(String email, String name, String password, String secondaryPassword,
		String profileImage) {
		return create(email, name, password, secondaryPassword, MemberRole.TEACHER, profileImage);
	}

	private static Member create(String email, String name, String password, String secondaryPassword,
		MemberRole role, String profileImage) {
		isValidEmail(email);
		Member member = new Member();
		member.email = email;
		member.name = name;
		member.memberRole = role;
		member.auth = new MemberAuth(password, secondaryPassword);
		member.profileImage = profileImage;
		return member;
	}

	@PrePersist
	private void setCreatedAtAndActivationStatus() {
		this.createdAt = LocalDateTime.now();
		this.activationStatus = 1;
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}

	public void updateLastLogin() {
		this.auth.updateLastLogin();
	}

	// 회원 탈퇴 시 호출
	public void updateActivationStatus() {
		this.activationStatus = 0;
	}
}

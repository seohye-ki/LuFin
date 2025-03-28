package com.lufin.server.member.domain;

import static com.lufin.server.member.util.MemberValidator.*;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

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
}

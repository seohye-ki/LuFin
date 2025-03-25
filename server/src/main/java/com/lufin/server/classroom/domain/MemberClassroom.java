package com.lufin.server.classroom.domain;

import java.time.LocalDateTime;

import com.lufin.server.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "member_classrooms",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"member_id", "classroom_id"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberClassroom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_classroom_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "classroom_id", nullable = false)
	private Classroom classroom;

	@Column(name = "join_date", nullable = false, updatable = false)
	private LocalDateTime joinDate;

	@Column(name = "is_current", nullable = false)
	private boolean isCurrent;

	// 정적 팩토리 메서드로 생성
	private MemberClassroom(Member member, Classroom classroom) {
		this.member = member;
		this.classroom = classroom;
		this.joinDate = LocalDateTime.now();
		this.isCurrent = true;
	}

	// 학급 구성원 등록 팩토리 메서드
	public static MemberClassroom enroll(Member member, Classroom classroom) {
		return new MemberClassroom(member, classroom);
	}

	// 현재 소속 여부를 false로 변경 (반 이동 등)
	public void deactivate() {
		this.isCurrent = false;
	}
}

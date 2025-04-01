package com.lufin.server.classroom.domain;

import static com.lufin.server.classroom.util.ClassroomValidator.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.lufin.server.member.domain.Member;
import com.lufin.server.mission.domain.Mission;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "classrooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Classroom {

	@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<MemberClassroom> memberClassrooms = new ArrayList<>();

	@OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = true)
	private final List<Mission> missions = new ArrayList<>();

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "classroom_id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "teacher_id", nullable = false)
	private Member teacher;

	@Column(name = "code", nullable = false, unique = true, length = 5)
	private String code;

	@Column(name = "school", nullable = false, length = 10)
	private String school;

	@Column(name = "grade", nullable = false)
	private Integer grade;

	@Column(name = "class_group", nullable = false)
	private Integer classGroup;

	@Column(name = "name", nullable = false, length = 20)
	private String name;

	@Column(name = "member_count", nullable = false)
	private Integer memberCount = 0;

	@Column(name = "thumbnail_key")
	private String thumbnailKey;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	// 학급 엔티티 생성자 (정적 팩토리에서 호출됨)
	private Classroom(Member teacher, String code, String school, Integer grade, Integer classGroup,
		String name, String thumbnailKey) {
		this.teacher = teacher;
		this.code = code;
		this.school = school;
		this.grade = grade;
		this.classGroup = classGroup;
		this.name = name;
		this.thumbnailKey = thumbnailKey;
		setCreatedAt();
	}

	// 학급 생성 -> 이미지 없을 시 기본 썸네일 저장
	public static Classroom create(Member teacher, String code, String school, Integer grade,
		Integer classGroup, String name, String thumbnailKey) {
		validateCreateClassroom(code, grade);
		thumbnailKey = prepareThumbnailKey(thumbnailKey);
		return new Classroom(teacher, code, school, grade, classGroup, name, thumbnailKey);
	}

	public void updateInfo(String school, int grade, int classGroup, String name, String key) {
		this.school = school;
		this.grade = grade;
		this.classGroup = classGroup;
		this.name = name;

		if (key != null) {
			this.thumbnailKey = prepareThumbnailKey(key);
		}
	}

	private static String prepareThumbnailKey(String thumbnailKey) {
		if (thumbnailKey != null) {
			validateThumbnailFileName(thumbnailKey);
		} else {
			thumbnailKey = "classrooms/302e3f79-083c-4e7c-adf7-a788d9b9c891.jpg";
		}
		return thumbnailKey;
	}

	// 학급에 구성원 추가
	public void addMemberClass(MemberClassroom memberClassroom) {
		this.memberClassrooms.add(memberClassroom);
		incrementMemberCount();
	}

	// 학급에서 구성원 제거
	public void removeMemberClass(MemberClassroom memberClassroom) {
		this.memberClassrooms.remove(memberClassroom);
		decrementMemberCount();
	}

	// 구성원 수 증가
	public void incrementMemberCount() {
		this.memberCount++;
	}

	// 구성원 수 감소 (0 이하로 내려가지 않음)
	public void decrementMemberCount() {
		if (this.memberCount > 0) {
			this.memberCount--;
		}
	}

	/* mission 연관관계 메서드 */
	public void addMission(Mission mission) {
		// 중복 체크 후 없으면 추가
		if (!missions.contains(mission)) {
			missions.add(mission);
		}

		// 무한루프 방지 체크 및 연관관계 설정
		if (mission.getClassroom() != this) {
			mission.setClassroom(this);
		}
	}

	public void removeMissions(Mission mission) {
		if (missions.contains(mission)) {
			missions.remove(mission);
		}
	}

	public void removeMission(Mission mission) {
		this.missions.remove(mission);
	}

	@PrePersist
	private void setCreatedAt() {
		if (this.createdAt == null) {
			this.createdAt = LocalDateTime.now();
		}
	}

	/* 무한루프 방지를 위한 contains()를 위해 id 기반으로 equals() 오버라이딩 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Classroom classroom = (Classroom)o;
		return id != null && id.equals(classroom.id);
	}

	@Override
	public int hashCode() {
		return id != null ? id.hashCode() : 0;
	}
}

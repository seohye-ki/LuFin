package com.lufin.server.mission.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "mission_images")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MissionImage {
	@ManyToOne
	@JoinColumn(name = "mission_id", nullable = false)
	private Mission mission;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mission_image_id", nullable = false)
	private Integer missionImageId;

	@Column(name = "bucket_name", nullable = false)
	private String bucketName;

	@Column(name = "object_key", nullable = false)
	private String objectKey;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@Transient // db에 저장 x
	private String presignedUrl;

	// 시간 설정
	@PrePersist
	private void setCreatedAt() {
		this.createdAt = LocalDateTime.now();
		this.updatedAt = this.createdAt;
	}

	@PreUpdate
	private void setUpdatedAt() {
		this.updatedAt = LocalDateTime.now();
	}


	/* 로직 */

	/**
	 * 미션 이미지 생성을 위한 팩토리 메서드
	 */
	public static MissionImage create(Mission mission, String bucketName, String objectKey) {
		return MissionImage.builder()
			.mission(mission)
			.bucketName(bucketName)
			.objectKey(objectKey)
			.build();
	}

	/**
	 * 미션과의 연관관계를 설정 (package-private)
	 * 이 메서드는 양방향 연관관계의 일관성을 유지하기 위해 사용
	 * @param mission 연관될 미션 (null이 아니어야 함)
	 */
	void setMission(Mission mission) {
		this.mission = mission;
		if (mission != null && !mission.getImages().contains(this)) {
			mission.addImage(this);
		}
	}

	// TODO: Service 제작 시 추가 필요
	public void generatePresignedUrl() {

	}

}

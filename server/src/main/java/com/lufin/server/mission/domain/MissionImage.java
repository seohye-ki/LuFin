package com.lufin.server.mission.domain;

import java.time.LocalDateTime;

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
	@ManyToOne(fetch = FetchType.LAZY)
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
	 * 양방향 연관관계의 일관성을 유지하기 위한 로직이 추가되어 있음
	 * @param mission 참여할 미션
	 * @param bucketName S3 버킷 이름
	 * @param objectKey S3 버킷의 객체 고유 키
	 * @return 생성된 미션 이미지 객체
	 */
	public static MissionImage create(Mission mission, String bucketName, String objectKey) {
		// service 레이어에서 호출 할 때 null이 들어올 수도 있기에 null 체크 실행
		if (mission == null) {
			throw new IllegalArgumentException("Mission cannot be null");
		}

		if (bucketName == null) {
			throw new IllegalArgumentException("Bucket name cannot be null");
		}

		if (objectKey == null) {
			throw new IllegalArgumentException("Object key cannot be null");
		}

		// 객체를 먼저 생성하고 메서드를 통해 양방향 관계 설정
		MissionImage image = MissionImage.builder()
			.bucketName(bucketName)
			.objectKey(objectKey)
			.build();

		mission.addImage(image);

		return image;
	}

	/**
	 * 미션과의 연관관계를 설정 (package-private)
	 * 이 메서드는 양방향 연관관계의 일관성을 유지하기 위해 사용
	 * @param mission 연관될 미션 (null이 아니어야 함)
	 */
	void setMission(Mission mission) {
		// 중복 체크는 addImage에서 진행
		this.mission = mission;
	}

	// TODO: Service 제작 시 추가 필요
	public void generatePresignedUrl() {
		//CHECK: setMission() 사용 예정
	}

	// 무한 루프 방지를 위해 contains()를 사용하면서 id를 기준으로 체크하기 위해 equals override
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		MissionImage that = (MissionImage)o;
		return missionImageId != null && missionImageId.equals(that.missionImageId);
	}

	@Override
	public int hashCode() {
		return missionImageId != null ? missionImageId.hashCode() : 0;
	}
}

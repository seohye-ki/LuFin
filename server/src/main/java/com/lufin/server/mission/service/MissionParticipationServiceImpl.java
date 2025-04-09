package com.lufin.server.mission.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.constants.HistoryStatus;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.credit.domain.CreditEventType;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.credit.service.CreditScoreService;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.domain.MemberRole;
import com.lufin.server.mission.domain.Mission;
import com.lufin.server.mission.domain.MissionParticipation;
import com.lufin.server.mission.domain.MissionParticipationStatus;
import com.lufin.server.mission.dto.MissionParticipationResponseDto;
import com.lufin.server.mission.repository.MissionParticipationRepository;
import com.lufin.server.mission.repository.MissionRepository;
import com.lufin.server.transaction.domain.TransactionSourceType;
import com.lufin.server.transaction.domain.TransactionType;
import com.lufin.server.transaction.service.TransactionHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MissionParticipationServiceImpl implements MissionParticipationService {

	public static int missionSuccessCreditScore = 2;
	public static int missionFailureCreditScore = -2;
	private final MissionRepository missionRepository;
	private final MissionParticipationRepository missionParticipationRepository;
	private final AccountRepository accountRepository;
	private final TransactionHistoryService transactionHistoryService;
	private final CreditScoreService creditScoreService;
	private final CreditService creditService;
	private final CreditScoreRepository creditScoreRepository;
	private final MemberClassroomRepository memberClassroomRepository;

	/**
	 * 미션 참여 신청
	 * @param classId
	 * @param missionId
	 * @param currentMember
	 * @return
	 */
	@Override
	@Transactional(isolation = Isolation.REPEATABLE_READ, rollbackFor = BusinessException.class)
	public MissionParticipationResponseDto.MissionApplicationResponseDto applyMission(Integer classId,
		Integer missionId,
		Member currentMember) {
		log.info("미션 참여 신청 요청: classId: {}, missionId: {}, currentMember: {}", classId, missionId, currentMember);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		// 학생이 아닐 경우 신청 불가
		if (currentMember.getMemberRole() != MemberRole.STUDENT) {
			throw new BusinessException(INVALID_ROLE_SELECTION);
		}

		try {
			// 비관적 락을 통해 동시성 문제 해결 시도
			Mission mission = missionRepository.findByIdAndClassIdWithPessimisticLock(missionId, classId)
				.orElseThrow(() -> new BusinessException(MISSION_NOT_FOUND));

			// 인원수가 다 찼으면 신청 불가능
			if (mission.getCurrentParticipants() >= mission.getMaxParticipants()) {
				throw new BusinessException(MISSION_CAPACITY_FULL);
			}

			// 이미 내가 신청한 미션이 있으면 불가능
			boolean memberMissionFlag = missionParticipationRepository.existsByMemberIdAndStatusInProgress(
				currentMember.getId(), classId);

			if (memberMissionFlag) {
				log.warn("미션 진행은 단 하나만 가능합니다. currentMember = {}, mission = {}", currentMember, mission);
				throw new BusinessException(MISSION_APPLICATION_ALREADY_EXISTS);
			}

			// MissionParticipation 엔티티 생성
			MissionParticipation newParticipation = MissionParticipation.create(
				mission,
				currentMember
			);

			MissionParticipation savedParticipation = missionParticipationRepository.save(newParticipation);

			return new MissionParticipationResponseDto.MissionApplicationResponseDto(
				savedParticipation.getParticipationId());
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/**
	 * 미션 참여자 목록 전체 조회
	 * @param classId
	 * @param missionId
	 * @param currentMember
	 * @return
	 */
	@Override
	public List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> getAllMissionParticipants(
		Integer classId,
		Integer missionId,
		Member currentMember
	) {
		log.info("미션 참여자 전체 목록 조회 요청: classId: {}, missionId: {}", classId, missionId);

		if (classId == null || missionId == null || currentMember == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			List<MissionParticipationResponseDto.MissionParticipationSummaryResponseDto> missionParticipants = missionParticipationRepository.getMissionParticipationList(
				classId,
				missionId
			);

			return missionParticipants;
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}

	}

	/**
	 * 미션 참여 상태 변경
	 * 미션 완료 검토 요청은 학생이 CHECKING으로 상태를 변경하는 요청임
	 * @param classId
	 * @param missionId
	 * @param participationId
	 * @param currentMember
	 * @param status
	 * @return
	 */
	@Transactional
	@Override
	public MissionParticipationResponseDto.MissionParticipationStatusResponseDto changeMissionParticipationStatus(
		Integer classId,
		Integer missionId,
		Integer participationId,
		Member currentMember,
		MissionParticipationStatus status
	) {
		log.info("미션 참여 상태 변경 요청: classId: {}, missionId: {}, role: {}, status: {}", classId, missionId,
			currentMember.getMemberRole(), status);

		if (classId == null || missionId == null || currentMember == null || status == null) {
			throw new BusinessException(MISSING_REQUIRED_VALUE);
		}

		try {
			MissionParticipation participation = missionParticipationRepository.findById(participationId)
				.orElseThrow(() -> new BusinessException(MEMBER_NOT_FOUND));

			// 같은 요청인지 확인
			if (participation.getStatus() == status) {
				log.warn("같은 상태입니다. status = {}", status);
				throw new BusinessException(INVALID_UPDATE_REQUEST);
			}

			// 해당 미션 참여가 요청한 미션에 속해 있는지 확인
			Mission mission = participation.getMission();
			if (mission == null
				|| !mission.getId().equals(missionId)
				|| !mission.getClassroom().getId().equals(classId)
			) {
				throw new BusinessException(INVALID_INPUT_VALUE);
			}

			MissionParticipationResponseDto.MissionParticipationStatusResponseDto response = null;

			// 1. 미션 성공 -> 보상 지급 (선생님만 가능)
			if (status == MissionParticipationStatus.SUCCESS) {
				if (currentMember.getMemberRole() == MemberRole.TEACHER) {
					response = updateMissionSuccess(
						classId,
						currentMember,
						mission,
						status,
						participation
					);
				} else {
					log.warn("권한 없는 상태 변경 시도: status = {}, memberId = {}", status, currentMember.getId());
					throw new BusinessException(FORBIDDEN_REQUEST);
				}
			} else {
				if (currentMember.getMemberRole() != MemberRole.TEACHER) {
					// 2. 교사가 아닌 경우
					if (status != MissionParticipationStatus.CHECKING) {
						// 2-1. CHECKING이 아닌 상태 변경은 교사만 가능
						log.warn("권한 없는 상태 변경 시도: status = {}, memberId = {}", status, currentMember.getId());
						throw new BusinessException(FORBIDDEN_REQUEST);
					} else if (!currentMember.getId().equals(participation.getMember().getId())) {
						// 2-2. 학생은 자신의 미션 참여만 변경 가능
						log.warn("타인의 미션 참여 상태 변경 시도: memberId = {}, participationMemberId = {}",
							currentMember.getId(), participation.getMember().getId());
						throw new BusinessException(REQUEST_DENIED);
					}
				}

				log.info(
					"미션 상태 변경 작업 시작: classId = {}, currentMember ={}, mission = {}, status = {}, participation = {} ",
					classId, currentMember.getId(), mission, status, participation);

				/* JPA 더티 체킹으로 객체에 먼저 변경 사항이 캐시된 후, transaction이 끝날 때 자동으로 쿼리를 통해 DB를 업데이트 */
				participation.changeMissionStatus(status);

				// MissionParticipation 엔티티를 DTO로 변환
				response = MissionParticipationResponseDto.MissionParticipationStatusResponseDto
					.missionParticipationToMissionParticipationStatusResponseDto(status);

				// creditScoreService가 별도의 트랜잭션으로 동작할 수 있기 때문에 모든 creditScoreService 예외에 대해 business 예외를 던지도록 로직 강화
				try {
					// 실패나 거절이면 신뢰도 차감
					if (status == MissionParticipationStatus.FAILED || status == MissionParticipationStatus.REJECTED) {
						log.info("신용 등급 작업 개시: member = {}, creditDeltaScore = {}", participation.getMember(),
							missionFailureCreditScore);

						creditScoreService.applyScoreChange(
							participation.getMember(),
							missionFailureCreditScore,
							CreditEventType.MISSION_FAILURE,
							classId
						);

						log.info("신용 등급 작업 완료: member = {} creditScore = {}", participation.getMember(),
							creditService.getScore(participation.getMember().getId(), classId));
					}
				} catch (Exception e) {
					throw new BusinessException(CREDIT_SCORE_UPDATE_FAILED);
				}

			}

			return response;

		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}
	}

	/**
	 * 미션 성공 처리
	 * @param classId
	 * @param currentMember
	 * @param mission
	 * @param status
	 * @param participation
	 */
	@TeacherOnly
	@Transactional(
		isolation = Isolation.REPEATABLE_READ,
		rollbackFor = BusinessException.class,
		timeout = 30 // 30초 안에 거래가 완료되지 않으면 타임아웃
	)
	public MissionParticipationResponseDto.MissionParticipationStatusResponseDto updateMissionSuccess(
		Integer classId,
		Member currentMember,
		Mission mission,
		MissionParticipationStatus status,
		MissionParticipation participation
	) {
		log.info("미션 성공 작업 시작: classId = {}, currentMember ={}, mission = {}, status = {}, participation = {} ",
			classId, currentMember.getId(), mission, status, participation);

		MemberClassroom memberClassroom = memberClassroomRepository.findByMemberIdAndClassroomIdAndIsCurrentTrue(
				currentMember.getId(), classId)
			.orElseThrow(() -> {
				log.warn("🔁[신용 점수 조회 실패] - 학생이 해당 클래스 소속이 아님");
				return new BusinessException(STUDENT_NOT_IN_TEACHER_CLASS);
			});

		try {
			// 참여자가 개인 계좌를 보유하고 있는지 체크
			Optional<Account> account = accountRepository.findOpenAccountByMemberIdWithPessimisticLock(
				participation.getMember().getId(), classId);

			if (!account.isPresent()) {
				log.warn("미션 완료 보상을 받을 계좌가 존재하지 않습니다.");
				throw new BusinessException(ACCOUNT_NOT_FOUND);
			}

			Account personalAccount = account.get();

			// 클래스 계좌가 있는지 확인
			Account classAccount = accountRepository.findByClassroomId(classId)
				.orElseThrow(() -> new BusinessException(ACCOUNT_NOT_FOUND));

			participation.changeMissionStatus(status); // 상태를 성공으로 변경
			log.info("상태 변경 완료: participation = {}", participation);

			log.info("계좌 작업 개시: classAccount = {}, personalAccount = {}, wage = {}", classAccount, personalAccount,
				mission.getWage());

			// 개인 계좌에 보상액 지급
			personalAccount.deposit(mission.getWage());
			accountRepository.save(personalAccount);

			// 클래스 계좌에 누적 총액 기록용 입금
			classAccount.deposit(mission.getWage());
			accountRepository.save(classAccount);

			log.info("계좌 입금 완료: 입금액 ={}, 잔액 = {}", mission.getWage(), personalAccount.getBalance());
			participation.markWagePaid(); // 입금 완료로 변경

			// creditScoreService가 별도의 트랜잭션으로 동작할 수 있기 때문에 모든 creditScoreService 예외에 대해 business 예외를 던지도록 로직 강화
			try {
				log.info("신용 등급 작업 개시: member = {}, creditDeltaScore = {}", participation.getMember(),
					missionSuccessCreditScore);

				creditScoreService.applyScoreChange(
					participation.getMember(),
					missionSuccessCreditScore,
					CreditEventType.MISSION_COMPLETION,
					classId
				);

				log.info("신용 등급 작업 완료: member = {} creditScore = {}", participation.getMember(),
					creditService.getScore(participation.getMember().getId(), classId));
			} catch (Exception e) {
				throw new BusinessException(CREDIT_SCORE_UPDATE_FAILED);
			}

			// 거래 내역 작성
			transactionHistoryService.record(
				personalAccount,
				classAccount.getAccountNumber(),
				participation.getMember(),
				mission.getWage(),
				personalAccount.getBalance(),
				TransactionType.DEPOSIT,
				HistoryStatus.SUCCESS,
				"미션 완료",
				TransactionSourceType.DEPOSIT
			);

			return MissionParticipationResponseDto.MissionParticipationStatusResponseDto
				.missionParticipationToMissionParticipationStatusResponseDto(status);
		} catch (TransactionTimedOutException tte) {
			log.error("미션 성공 작업 중 타임 아웃 발생: {}", tte.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage());
			throw new BusinessException(SERVER_ERROR);
		}
	}

}

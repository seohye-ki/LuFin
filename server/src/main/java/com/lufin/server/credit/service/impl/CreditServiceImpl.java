package com.lufin.server.credit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.lufin.server.credit.dto.CreditHistoryDto;
import com.lufin.server.credit.repository.CreditScoreHistoryRepository;
import com.lufin.server.credit.repository.CreditScoreRepository;
import com.lufin.server.credit.service.CreditService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

	private final CreditScoreRepository creditRepository;
	private final CreditScoreHistoryRepository historyRepository;

	@Override
	public int getScore(int memberId) {
		log.info("[신용 점수 조회] memberId: {}", memberId);
		return creditRepository.findByMemberId(memberId)
			.map(c -> c.getScore().intValue())
			.orElse(0);
	}

	@Override
	public String getGrade(int memberId) {
		log.info("[신용 등급 조회] memberId: {}", memberId);
		return creditRepository.findByMemberId(memberId)
			.map(score -> score.getGrade().getDisplay())
			.orElse("N/A");
	}

	@Override
	public List<CreditHistoryDto> getGradeChangeHistory(int memberId) {
		log.info("[신용 점수 이력 조회] memberId: {}", memberId);
		return historyRepository.findTop10ByMemberIdOrderByCreatedAtDesc(memberId).stream()
			.map(h -> CreditHistoryDto.builder()
				.scoreChange(h.getScoreChange())
				.reason(h.getReason())
				.changedAt(h.getCreatedAt().toLocalDate())
				.build())
			.toList();
	}
}

package com.lufin.server.member.service;

import java.util.List;

import com.lufin.server.member.dto.RankingDto;

public interface RankingService {
	List<RankingDto> getAssetRanking();
}

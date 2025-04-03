package com.lufin.server.member.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.lufin.server.account.service.AccountService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.member.repository.MemberRepository;
import com.lufin.server.member.service.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

	private final MemberRepository memberRepository;
	private final AccountService accountService;

	@Override
	public List<RankingDto> getAssetRanking() {
		log.info("[자산 랭킹 확인]");
		List<Member> members = memberRepository.findAll();

		List<MemberAsset> sorted = members.stream()
			.map(member -> {
				int asset = accountService.getTotalAsset(member.getId());
				return new MemberAsset(member.getId(), member.getName(), asset);
			})
			.sorted(Comparator.comparingInt(MemberAsset::asset).reversed())
			.toList();

		return IntStream.range(0, sorted.size())
			.mapToObj(i -> {
				MemberAsset m = sorted.get(i);
				return RankingDto.builder()
					.memberId(m.id())
					.name(m.name())
					.asset(m.asset())
					.rank(i + 1)
					.build();
			})
			.toList();
	}

	private record MemberAsset(int id, String name, int asset) {
	}
}

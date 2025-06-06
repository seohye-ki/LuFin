package com.lufin.server.member.service.impl;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.springframework.stereotype.Service;

import com.lufin.server.auth.service.LoginFacadeService;
import com.lufin.server.classroom.domain.MemberClassroom;
import com.lufin.server.classroom.repository.MemberClassroomRepository;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.dto.RankingDto;
import com.lufin.server.member.service.RankingService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

	private final LoginFacadeService loginFacadeService;
	private final MemberClassroomRepository classroomRepository;

	@Override
	public List<RankingDto> getAssetRanking(int classId) {
		log.info("[반별 자산 랭킹 조회] classId={}", classId);

		// 해당 클래스에 속한 구성원 중 학생만 필터링
		List<MemberClassroom> students = classroomRepository.findStudentsByClassId(classId);

		// 각 학생의 자산 정보를 계산하고 TOP 10 내림차순 정렬
		List<MemberAsset> sorted = students.stream()
			.map(mc -> {
				Member member = mc.getMember();
				int asset = loginFacadeService.getTotalAsset(member.getId(), classId);
				return new MemberAsset(member.getId(), member.getName(), asset, member.getProfileImage());
			})
			.sorted(Comparator.comparingInt(MemberAsset::asset).reversed())
			.limit(10)
			.toList();

		// 랭킹 1위부터 순서대로 부여
		return IntStream.range(0, sorted.size())
			.mapToObj(i -> {
				MemberAsset m = sorted.get(i);
				return RankingDto.builder()
					.memberId(m.id())
					.name(m.name())
					.profileImage(m.image())
					.asset(m.asset())
					.rank(i + 1)
					.build();
			})
			.toList();
	}

	private record MemberAsset(int id, String name, int asset, String image) {
	}
}

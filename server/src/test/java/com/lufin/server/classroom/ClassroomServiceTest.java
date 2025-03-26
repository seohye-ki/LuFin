package com.lufin.server.classroom;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.classroom.service.ClassroomService;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.repository.MemberRepository;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class ClassroomServiceTest {

	@Autowired
	private ClassroomService classroomService;
	@Autowired
	private ClassroomRepository classroomRepository;
	@Autowired
	private MemberRepository memberRepository;

	@Test
	void createSuccess() {
		// given
		Member teacher = Member.createTeacher("teacher@email.com", "김루핀", "Qlalf123!@#", "123456");
		memberRepository.save(teacher);

		ClassRequest request = new ClassRequest(
			"싸피반", "서울초등학교", 4, 2, "class.png", "image/png"
		);

		// when
		ClassResponse response = classroomService.createClassroom(request, teacher);

		// then
		Classroom saved = classroomRepository.findById(response.classId()).orElseThrow();

		assertThat(saved.getName()).isEqualTo("싸피반");
		assertThat(saved.getSchool()).isEqualTo("서울초등학교");
		assertThat(saved.getGrade()).isEqualTo(4);
		assertThat(saved.getClassGroup()).isEqualTo(2);
		assertThat(saved.getCode()).isEqualTo(response.code());
	}

	@Test
	void createFailure() {
		// given
		Member student = Member.createStudent("student@email.com", "김싸피", "Qlalf123!@#", "123456");
		memberRepository.save(student);

		ClassRequest request = new ClassRequest(
			"열정반", "부산초등학교", 5, 1, "logo.png", "image/png"
		);

		// when & then
		assertThatThrownBy(() -> classroomService.createClassroom(request, student))
			.isInstanceOf(BusinessException.class);
	}
}

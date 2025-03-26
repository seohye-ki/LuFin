package com.lufin.server.classroom.service;

import com.lufin.server.classroom.dto.ClassRequest;
import com.lufin.server.classroom.dto.ClassResponse;
import com.lufin.server.member.domain.Member;

public interface ClassroomService {

	ClassResponse createClassroom(ClassRequest request, Member teacher);
}

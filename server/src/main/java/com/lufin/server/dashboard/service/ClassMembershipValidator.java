package com.lufin.server.dashboard.service;

public interface ClassMembershipValidator {

	void validateTeacherAccessToStudent(Integer memberId, Integer studentId, Integer classId);

	void validateStudentInClass(int studentId, int classId);
}

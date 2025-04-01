package com.lufin.server.classroom.dto;

public record ClassResponse(int classId, String name, String school,
							int year, int grade, int classGroup,
							String code, int memberCount, int balance, String key) {
}

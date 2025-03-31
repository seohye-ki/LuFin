package com.lufin.server.classroom.dto;

public record FindClassesResponse(String name, String school, int year,
								  int grade, int classGroup,
								  int memberCount, String key) {
}

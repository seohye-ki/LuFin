package com.lufin.server.classroom.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClassResponse(@NotBlank int classId, @NotBlank String name, @NotBlank String school,
							@Min(2024) int year, @Min(4) @Max(6) int grade, @Min(1) int classGroup,
							@NotBlank String code) {
}

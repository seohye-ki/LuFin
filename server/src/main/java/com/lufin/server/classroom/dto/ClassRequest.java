package com.lufin.server.classroom.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ClassRequest(@NotBlank String name, @NotBlank String school, @Min(4) @Max(6) int grade,
						   @Min(1) int classGroup, String fileName, String fileType) {
}

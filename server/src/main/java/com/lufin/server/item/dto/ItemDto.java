package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemDto(
	@NotBlank
	String name,

	@NotNull
	Integer type,

	@NotNull
	@Min(value = 0)
	Integer price,

	@NotNull
	@Min(value = 0)
	Integer quantityAvailable,

	@Future
	@JsonFormat(pattern = "yyyy-MM-dd")
	LocalDateTime expirationDate
) { }

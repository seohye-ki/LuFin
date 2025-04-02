package com.lufin.server.item.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ItemDto(
	@NotBlank
	@Size(min = 2, max = 30)
	String name,

	@NotNull
	@Min(0)
	@Max(1)
	Integer type,

	@NotNull
	@Min(100)
	@Max(10000000)
	Integer price,

	@NotNull
	@Min(1)
	@Max(1000)
	Integer quantityAvailable,

	@NotNull
	@FutureOrPresent
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	LocalDateTime expirationDate
) { }

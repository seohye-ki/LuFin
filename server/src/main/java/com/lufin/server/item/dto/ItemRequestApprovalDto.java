package com.lufin.server.item.dto;

import com.lufin.server.item.domain.ItemRequestStatus;

import jakarta.validation.constraints.NotNull;

public record ItemRequestApprovalDto(
	@NotNull
	ItemRequestStatus status
) {
}

package com.lufin.server.item.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPurchaseRequestDto(

	@NotNull
	Integer itemId,

	@NotNull
	@Min(1)
	Integer itemCount

) {
}

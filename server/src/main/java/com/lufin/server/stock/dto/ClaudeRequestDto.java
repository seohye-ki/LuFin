package com.lufin.server.stock.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaudeRequestDto {
	private String model;
	private List<Message> messages;
	private Integer max_tokens;
	;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Message {
		private String role;
		private String content;
	}
}
package com.lufin.server.stock.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaudeResponseDto {
	private String id;
	private String type;
	private String role;
	private List<Content> content;
	private String model;
	private String stop_reason;
	private Usage usage;

	@Data
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Content {
		private String type;
		private String text;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Usage {
		private Integer input_tokens;
		private Integer output_tokens;
	}
}
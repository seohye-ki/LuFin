package com.lufin.server.image.dto;

public record PresignedUrlResponse(String uploadUrl, String key) {
}

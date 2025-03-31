package com.lufin.server.image.service;

import static com.lufin.server.common.constants.ErrorCode.*;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.lufin.server.common.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {

	private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpg");
	private final AmazonS3 amazonS3;
	@Value("${cloud.aws.s3.bucket}")
	private String bucketName;

	public String generatePresignedUrlWithKey(String key, String extension) {
		if (!ALLOWED_EXTENSIONS.contains(extension)) {
			throw new BusinessException(INVALID_EXTENSION);
		}

		Date expiration = new Date(System.currentTimeMillis() + (1000 * 60 * 5)); // 5분 유효

		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key)
			.withMethod(HttpMethod.PUT)
			.withExpiration(expiration)
			.withContentType(getMimeType(extension));

		URL url = amazonS3.generatePresignedUrl(request);
		return url.toString();
	}

	public String getExtension(String filename) {
		return Optional.ofNullable(filename)
			.filter(f -> f.contains("."))
			.map(f -> f.substring(filename.lastIndexOf(".") + 1))
			.map(String::toLowerCase)
			.orElse("");
	}

	public String getMimeType(String extension) {
		return switch (extension) {
			case "jpg", "jpeg" -> "image/jpeg";
			case "png" -> "image/png";
			default -> "application/octet-stream";
		};
	}
}

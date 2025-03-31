package com.lufin.server.image.conrtroller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.image.dto.PresignedUrlResponse;
import com.lufin.server.image.service.S3Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/files")
public class FileController {

	private final S3Service s3Service;

	@GetMapping("/presigned-url")
	public ResponseEntity<PresignedUrlResponse> getPresignedUrl(
		@RequestParam String folder,
		@RequestParam String filename) {

		String extension = s3Service.getExtension(filename);
		String key = folder + "/" + UUID.randomUUID() + "." + extension;
		String uploadUrl = s3Service.generatePresignedUrlWithKey(key, extension);

		return ResponseEntity.ok(new PresignedUrlResponse(uploadUrl, key));
	}

	@GetMapping("/download-url")
	public ResponseEntity<PresignedUrlResponse> getDownloadUrl(@RequestParam String key) {
		String url = s3Service.generateDownloadUrl(key);
		return ResponseEntity.ok(new PresignedUrlResponse(url, key));
	}
}

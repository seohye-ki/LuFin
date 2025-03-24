package com.lufin.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.exception.type.BadRequestException;
import com.lufin.server.common.exception.type.ConflictException;
import com.lufin.server.common.exception.type.ForbiddenException;
import com.lufin.server.common.exception.type.InternalServerErrorException;
import com.lufin.server.common.exception.type.NotFoundException;
import com.lufin.server.common.exception.type.ServiceUnavailableException;
import com.lufin.server.common.exception.type.UnauthorizedException;
import com.lufin.server.common.exception.type.UnprocessableEntityException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// 400 Bad Request 처리
	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ApiResponse<Void>> handleBadRequestException(BadRequestException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 401 Unauthorized 처리
	@ExceptionHandler(UnauthorizedException.class)
	public ResponseEntity<ApiResponse<Void>> handleUnauthorizedException(UnauthorizedException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 403 Forbidden 처리
	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<ApiResponse<Void>> handleForbiddenException(ForbiddenException e) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 404 Not Found 처리
	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ApiResponse<Void>> handleNotFoundException(NotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 409 Conflict 처리
	@ExceptionHandler(ConflictException.class)
	public ResponseEntity<ApiResponse<Void>> handleConflictException(ConflictException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 422 Unprocessable Entity 처리
	@ExceptionHandler(UnprocessableEntityException.class)
	public ResponseEntity<ApiResponse<Void>> handleUnprocessableEntityException(UnprocessableEntityException e) {
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 500 Internal Server Error 처리
	@ExceptionHandler(InternalServerErrorException.class)
	public ResponseEntity<ApiResponse<Void>> handleInternalServerErrorException(InternalServerErrorException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.failure(e.getErrorCode()));
	}

	// 503 Service Unavailable 처리
	@ExceptionHandler(ServiceUnavailableException.class)
	public ResponseEntity<ApiResponse<Void>> handleServiceUnavailableException(ServiceUnavailableException e) {
		return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ApiResponse.failure(e.getErrorCode()));
	}
}

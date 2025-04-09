package com.lufin.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.common.exception.type.BadRequestException;
import com.lufin.server.common.exception.type.ConflictException;
import com.lufin.server.common.exception.type.ForbiddenException;
import com.lufin.server.common.exception.type.InternalServerErrorException;
import com.lufin.server.common.exception.type.NotFoundException;
import com.lufin.server.common.exception.type.ServiceUnavailableException;
import com.lufin.server.common.exception.type.UnauthorizedException;
import com.lufin.server.common.exception.type.UnprocessableEntityException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	// BuisnessException 처리
	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
		log.warn("BusinessException 발생: {}", e.getErrorCode());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(e.getErrorCode()));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse<Void>> handleJsonParseException(HttpMessageNotReadableException e) {
		log.warn("역직렬화 실패 - 요청 본문 확인 필요: {}", e.getMostSpecificCause().getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(ErrorCode.INVALID_ENUM));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException e) {
		log.error("DTO 입력 값 누락: {}", e.getMessage());
		for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
			log.warn("검증 실패 - 필드: {}, 메시지: {}", fieldError.getField(), fieldError.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.failure(ErrorCode.MISSING_REQUIRED_VALUE));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleUncaughtException(Exception e) {
		log.error("처리되지 않은 예외 발생", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(ApiResponse.failure(ErrorCode.SERVER_ERROR));
	}
}

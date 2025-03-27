package com.lufin.server.websocket.error;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Component;

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

@Component
@Slf4j
public class WebSocketExceptionHandler {

	// WebSocketException 전용 핸들러 추가
	@MessageExceptionHandler(WebSocketException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleWebSocketException(WebSocketException e, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket 예외 발생: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 400 Bad Request 처리
	@MessageExceptionHandler(BadRequestException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleBadRequestException(BadRequestException e,
		SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Bad Request 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 401 Unauthorized 처리
	@MessageExceptionHandler(UnauthorizedException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleUnauthorizedException(UnauthorizedException e,
		SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Unauthorized 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 403 Forbidden 처리
	@MessageExceptionHandler(ForbiddenException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleForbiddenException(ForbiddenException e, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Forbidden 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 404 Not Found 처리
	@MessageExceptionHandler(NotFoundException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleNotFoundException(NotFoundException e, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Not Found 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 409 Conflict 처리
	@MessageExceptionHandler(ConflictException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleConflictException(ConflictException e, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Conflict 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 422 Unprocessable Entity 처리
	@MessageExceptionHandler(UnprocessableEntityException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleUnprocessableEntityException(UnprocessableEntityException e,
		SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Unprocessable Entity 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 500 Internal Server Error 처리
	@MessageExceptionHandler(InternalServerErrorException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleInternalServerErrorException(InternalServerErrorException e,
		SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Internal Server Error 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 503 Service Unavailable 처리
	@MessageExceptionHandler(ServiceUnavailableException.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleServiceUnavailableException(ServiceUnavailableException e,
		SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket Service Unavailable 예외: {}, 세션 ID: {}", e.getMessage(), sessionId);
		return ApiResponse.failure(e.getErrorCode());
	}

	// 그 외 예외 처리
	@MessageExceptionHandler(Exception.class)
	@SendToUser("/queue/errors")
	public ApiResponse<Void> handleException(Exception e, SimpMessageHeaderAccessor headerAccessor) {
		String sessionId = headerAccessor != null ? headerAccessor.getSessionId() : "unknown";
		log.error("💡WebSocket 처리되지 않은 예외: {}, 세션 ID: {}", e.getMessage(), sessionId, e);
		return ApiResponse.failure(ErrorCode.SERVER_ERROR);
	}
}

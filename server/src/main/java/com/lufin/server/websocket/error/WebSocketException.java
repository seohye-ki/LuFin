package com.lufin.server.websocket.error;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class WebSocketException extends BusinessException {

	public WebSocketException(ErrorCode errorCode) {
		super(errorCode);
	}
}

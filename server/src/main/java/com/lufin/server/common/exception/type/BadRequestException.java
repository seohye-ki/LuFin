package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class BadRequestException extends BusinessException {

	public BadRequestException(ErrorCode errorCode) {
		super(errorCode);
	}
}

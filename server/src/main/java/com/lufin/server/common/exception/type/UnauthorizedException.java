package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class UnauthorizedException extends BusinessException {

	public UnauthorizedException(ErrorCode errorCode) {
		super(errorCode);
	}
}

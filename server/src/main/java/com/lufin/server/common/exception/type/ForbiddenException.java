package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class ForbiddenException extends BusinessException {

	public ForbiddenException(ErrorCode errorCode) {
		super(errorCode);
	}
}

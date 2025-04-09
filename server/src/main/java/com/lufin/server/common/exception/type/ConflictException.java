package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class ConflictException extends BusinessException {

	public ConflictException(ErrorCode errorCode) {
		super(errorCode);
	}
}

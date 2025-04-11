package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class UnprocessableEntityException extends BusinessException {

	public UnprocessableEntityException(ErrorCode errorCode) {
		super(errorCode);
	}
}

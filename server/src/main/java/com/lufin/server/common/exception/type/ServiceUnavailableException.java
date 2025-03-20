package com.lufin.server.common.exception.type;

import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;

public class ServiceUnavailableException extends BusinessException {

    public ServiceUnavailableException(ErrorCode errorCode) {
        super(errorCode);
    }
}


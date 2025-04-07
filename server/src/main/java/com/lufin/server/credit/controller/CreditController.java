package com.lufin.server.credit.controller;

import static com.lufin.server.common.utils.ValidationUtils.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.common.annotation.TeacherOnly;
import com.lufin.server.common.dto.ApiResponse;
import com.lufin.server.credit.dto.CreditScoreDto;
import com.lufin.server.credit.service.CreditService;
import com.lufin.server.member.domain.Member;
import com.lufin.server.member.support.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lufin/credit")
public class CreditController {
	private static final String CLASS_ID = "classId";

	private final CreditService creditService;

	@TeacherOnly
	@PatchMapping("/recovery/{memberId}")
	public ResponseEntity<ApiResponse<CreditScoreDto>> recoverCreditStatus(HttpServletRequest httpRequest,
		@PathVariable int memberId) {
		Integer classId = (Integer) httpRequest.getAttribute(CLASS_ID);
		validateClassId(classId);
		Member member = UserContext.get();
		CreditScoreDto result = creditService.recoverCreditStatus(member, memberId, classId);
		return ResponseEntity.ok(ApiResponse.success(result));
	}
}

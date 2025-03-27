package com.lufin.server.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	MISSING_REQUIRED_VALUE("E400001", "필수 입력 값이 누락되었습니다."),
	INVALID_PASSWORD_FORMAT("E400002", "비밀번호는 최소 8자리 이상이어야 하며, 특수문자를 포함해야 합니다."),
	INVALID_EMAIL_FORMAT("E400003", "잘못된 이메일 형식입니다."),
	INVALID_ROLE_SELECTION("E400004", "정확한 역할을 선택해주세요."),
	INSUFFICIENT_STOCK("E400005", "아이템 재고가 부족합니다."),
	INVALID_PURCHASE_QUANTITY("E400006", "유효하지 않은 구매 수량입니다."),
	INVALID_INPUT_VALUE("E400007", "잘못된 입력 값입니다."),
	INVALID_DEPOSIT_AMOUNT("E400008", "입금 금액은 0보다 커야 합니다."),
	INVALID_WITHDRAWAL_AMOUNT("E400009", "출금 금액은 0보다 커야 합니다."),
	CLASS_CODE_INVALID("E400010", "클래스 입장 코드가 잘못되었습니다."),
	INVALID_FILE_NAME_FORMAT("E400011", "파일 이름 형식이 잘못되었습니다."),
	INVALID_REFUND_CONDITION("E400012", "해당 구매 내역은 환불할 수 없습니다."),

	INVALID_CREDENTIALS("E401001", "이메일 또는 패스워드를 확인해주세요."),
	UNAUTHORIZED_ACCESS("E401002", "인증이 필요합니다."),
	TOKEN_EXPIRED("E401003", "인증이 만료되었습니다. 다시 로그인해 주세요."),
	INVALID_AUTH_HEADER("E401004", "Authorization 헤더가 없거나 형식이 잘못되었습니다."),
	INVALID_TOKEN("E401005", "유효하지 않은 토큰입니다."),
	INVALID_TOKEN_TYPE("E401006", "토큰 타입이 잘못되었습니다."),

	FORBIDDEN_REQUEST("E403001", "요청을 처리할 수 없습니다."),
	REQUEST_DENIED("E403002", "요청이 거부되었습니다."),
	MEMBER_ALREADY_DELETED("E403003", "탈퇴된 사용자입니다."),

	CLASS_NOT_FOUND("E404001", "해당 클래스가 존재하지 않습니다."),
	PURCHASE_RECORD_NOT_FOUND("E404002", "해당 구매 내역을 찾을 수 없습니다."),
	MEMBER_NOT_FOUND("E404003", "사용자를 찾을 수 없습니다."),
	INVESTMENT_PRODUCT_NOT_FOUND("E404004", "해당 투자 상품을 찾을 수 없습니다."),
	MISSION_NOT_FOUND("E404005", "해당 미션을 찾을 수 없습니다."),
	LOAN_APPLICATION_NOT_FOUND("E404006", "해당 대출 신청 정보를 찾을 수 없습니다."),
	ITEM_NOT_FOUND("E404007", "해당 아이템을 찾을 수 없습니다."),
	LOAN_PRODUCT_NOT_FOUND("E404008", "해당 대출 상품을 찾을 수 없습니다."),
	ACCOUNT_NOT_FOUND("E404009", "계좌를 찾을 수 없습니다."),

	EMAIL_ALREADY_REGISTERED("E409001", "이미 가입된 이메일입니다."),
	DUPLICATE_COUPON_USAGE("E409002", "이미 등록된 교번/학번 입니다."),

	INSUFFICIENT_CREDIT_SCORE("E422001", "신용 점수가 부족하여 이용할 수 없습니다."),
	INSUFFICIENT_BALANCE("E422002", "잔액이 부족합니다."),

	SERVER_ERROR("E500001", "요청을 처리할 수 없습니다. 다시 시도해 주세요."),
	SERVICE_UNAVAILABLE("E503001", "현재 서비스를 이용할 수 없습니다. 잠시 후 다시 시도해 주세요.");

	private final String code;
	private final String message;
}

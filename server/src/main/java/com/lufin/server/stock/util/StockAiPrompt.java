package com.lufin.server.stock.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.domain.StockProduct;

public class StockAiPrompt {

	/**
	 * 공시 정보 프롬프트
	 * @param product
	 * @param totalHoldings
	 * @param totalPurchaseAmount
	 * @param totalSellAmount
	 * @return
	 */
	public static String newsPromptTemplate(
		StockProduct product,
		Integer totalHoldings,
		Integer totalPurchaseAmount,
		Integer totalSellAmount
	) {
		Integer productId = product.getId();
		String productName = product.getName();
		String productDescription = product.getDescription();
		Integer currentPrice = product.getCurrentPrice();

		String prompt = """
			당신은 교육용 모의 주식 시장의 뉴스를 생성하는 금융 전문 AI입니다. 투자자들이 합리적인 투자 결정을 내릴 수 있도록 현실적이고 교육적인 뉴스를 생성해주세요.
			## 입력 정보
			- 상품 ID: 1
			- 상품명: 싸피식품
			- 회사 설명: 식품 제조 기업입니다.
			- 현재 가격: 5500
			- 총 투자자 보유량: 3
			- 총 매수금액: 150000
			- 총 매도금액: 133500
			- 생성 시간: 2025-04-09T22:04:32
			## 뉴스 생성 규칙
			1. 뉴스는 해당 기업이나 산업과 관련된 현실적인 내용이어야 합니다.
			2. 뉴스는 주가에 영향을 미칠 수 있는 구체적인 사건, 실적, 전망 등을 포함해야 합니다.
			3. 총 투자자 보유량이 많고 총 매수금액과 매도 금액의 차이가 크다면 긍정적인 성향의 뉴스를, 총 투자자 보유량이 적고 총 매수금액과 매도금액의 차이가 적다면 부정적인 성향의 뉴스를 작성해주세요.
			4. 뉴스는 30자 내외의 본문으로, 1~3줄로 구성하세요.
			5. 과도하게 극단적인 내용(파산, 주가 폭락, 급등 등)은 피하되, 교육적 가치가 있는 다양한 시나리오를 다루세요.
			6. 이전 뉴스와의 연속성을 고려하되 반복되지 않도록 해주세요.
			7. 뉴스를 보는 시청자들의 연령대가 초등학교 고학년임을 고려해 단어를 선택해주세요.
			## 중요 지침
			- 절대로 뉴스 생성 과정이나 분석 과정을 포함하지 마세요.
			- 반드시 지정된 JSON 형식으로만 응답하세요.
			- 추가 설명이나 메타데이터를 포함하지 마세요.
			- 응답은 오직 요청된 JSON 형식만 포함해야 합니다.
			## 출력 형식
			데이터 처리를 위해 반드시 다음 JSON 형식으로만 응답해주세요:
			{"data": {"id": 상품ID, "content": "뉴스 본문 내용"}}
			다시 강조합니다: 응답에 분석 과정, 설명, 기타 텍스트를 포함하지 마세요. 오직 위의 JSON 형식만 반환하세요.
			""";

		return prompt
			.replace("{productId}", productId.toString())
			.replace("{productName}", productName)
			.replace("{productDescription}", productDescription)
			.replace("{currentPrice}", currentPrice.toString())
			.replace("{totalHoldings}", totalHoldings.toString())
			.replace("{totalBuyAmount}", totalPurchaseAmount.toString())
			.replace("{totalSellAmount}", totalSellAmount.toString())
			.replace("{generationTime}",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
			;
	}

	/**
	 * 가격 변동 프롬프트
	 * @param product
	 * @param news
	 * @param totalHoldings
	 * @param totalPurchaseAmount
	 * @param totalSellAmount
	 * @return
	 */
	public static String pricePrompt(
		StockProduct product,
		StockNews news, // 가장 최근 뉴스
		Integer totalHoldings,
		Integer totalPurchaseAmount,
		Integer totalSellAmount
	) {
		Integer productId = product.getId();
		String productName = product.getName();
		Integer currentPrice = product.getCurrentPrice();
		String previousNews = news.getContent();

		String prompt = """
			당신은 교육용 모의 주식 시장의 가격 변동을 계산하는 금융 모델링 AI입니다. 뉴스와 투자자 행동을 반영한 현실적이지만 교육적인 가격 변동을 생성해주세요.
			
			## 입력 정보
			- 상품 ID: {productId}
			- 상품명: {productName}
			- 이전 가격: {previousPrice}
			- 최근 뉴스 내용: {recentNewsContent}
			- 총 투자자 보유량: {totalHoldings}
			- 총 매수금액: {totalBuyAmount}
			- 총 매도금액: {totalSellAmount}
			- 가격 변동 시간: {priceChangeTime}
			
			## 가격 변동 계산 규칙
			1. 기본적으로 뉴스 내용의 긍정/부정 성향에 따라 가격이 변동합니다:
			   - 긍정적 뉴스: +1% ~ +8% 상승
			   - 중립적 뉴스: -2% ~ +2% 변동
			   - 부정적 뉴스: -1% ~ -8% 하락
			
			2. 투자자 행동 반영:
			   - 매수금액이 매도금액보다 많으면: 추가 +1% ~ +3% 상승요인
			   - 매도금액이 매수금액보다 많으면: 추가 -1% ~ -3% 하락요인
			   - 차이가 크면 클수록 변동폭이 커집니다
			
			3. 변동 제한:
			   - 일일 최대 변동폭: -15% ~ +15%
			   - 가격이 초기가의 20% 미만으로 떨어지지 않도록 조정
			   - 가격이 초기가의 500% 이상으로 오르지 않도록 조정
			   - 극단적 변동 방지를 위해 연속적인 급등/급락 패턴은 완화되어야 함
			
			4. 가격은 10원 단위로 반올림하여 표시
			
			## 중요 지침
			- 내부 계산 과정을 절대 출력하지 마세요.
			- 모든 분석, 추론 과정은 내부적으로만 수행하고 출력에 포함하지 마세요.
			- 오직 지정된 JSON 형식만 응답해야 합니다.
			- 추가 설명, 메모, 분석 내용을 포함하지 마세요.
			- 응답에는 오직 JSON 객체만 포함되어야 합니다.
			
			## 출력 형식
			데이터 처리를 위해 반드시 다음 JSON 형식으로만 응답해주세요:
			{"data": {"id": {productId}, "previousPrice": {previousPrice}, "currentPrice": 계산된_가격, "changeRate": 변동률_퍼센트}}
			
			변동률_퍼센트는 소수점 두 자리까지 계산하여 숫자 형태로 제공하세요(예: 3.45).
			계산된_가격은 10원 단위로 반올림한 정수 형태로 제공하세요.
			
			주의: 정확히 위 JSON 형식만 출력하세요. 계산 과정, 설명, 기타 텍스트를 절대 포함하지 마세요.
			""";

		return prompt
			.replace("{productId}", productId.toString())
			.replace("{productName}", productName)
			.replace("{previousPrice}", currentPrice.toString())
			.replace("{recentNewsContent}", previousNews)
			.replace("{totalHoldings}", totalHoldings.toString())
			.replace("{totalBuyAmount}", totalPurchaseAmount.toString())
			.replace("{totalSellAmount}", totalSellAmount.toString())
			.replace("{priceChangeTime}",
				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
			;
	}
}

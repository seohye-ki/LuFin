package com.lufin.server.stock.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.lufin.server.stock.domain.StockNews;
import com.lufin.server.stock.domain.StockProduct;

public class StockAiPrompt {
	public static String newsPromptPrefix() {
		String prefix = """
			당신은 모의 주식 시장의 뉴스를 생성하는 금융 뉴스 전문가입니다. 교육 목적의 모의 투자 플랫폼에 적합한 가상 기업 뉴스를 생성해 주세요.
			
			## 뉴스 생성 지침
			1. 뉴스는 해당 기업의 산업 분야에 적합한 현실적인 내용이어야 합니다.
			2. 다음 시간대의 주가 변동에 논리적 근거가 될 수 있는 내용을 포함하세요.
			3. 구매량이 판매량보다 많으면 긍정적인 뉴스를, 판매량이 구매량보다 많으면 부정적인 뉴스를 생성하는 경향을 보이되, 항상 그렇지는 않게 해주세요.
			4. 뉴스는 30자 내외, 1~3줄의 본문으로 구성하세요.
			5. 모든 뉴스는 사실처럼 들리되 지나치게 극단적이지 않아야 합니다.
			6. 가끔은 중립적인 뉴스도 포함하여 변동성을 줄 수 있습니다.
			7. 실제 기업이나 인물의 이름은 사용하지 마세요.
			
			## 뉴스 유형 예시 (다양하게 활용)
			- 신제품/서비스 출시 또는 개발 소식
			- 분기별 실적 발표
			- 산업 트렌드 변화
			- 경영진 교체
			- 투자 유치 또는 확장 계획
			- 기술 혁신 또는 특허 취득
			- 파트너십 체결
			- 규제 변화
			- 시장 점유율 변동
			- 국내외 경제 환경 영향
			
			## 출력 형식
			<< news >> {"data": [{"id": 1, "content": "뉴스 본문 내용"}, {"id": 2, "content": "뉴스 본문 내용"}...]}
			""";
		return prefix;
	}

	public static String newsPromptTemplate(
		StockProduct product,
		StockNews news, // 가장 최근 뉴스
		Integer totalPurchaseAmount,
		Integer totalSellAmount
	) {
		Integer productId = product.getId();
		String productName = product.getName();
		String productDescription = product.getDescription();
		Integer currentPrice = product.getCurrentPrice();
		String previousNews = news.getContent();

		String prompt = """
			## 입력 정보
			- 상품 ID: {product_id}
			- 상품명: {product_name}
			- 상품 설명: {product_description}
			- 현재 주가: {current_price}
			- 이전 뉴스: {previous_news}
			- 최근 구매량: {recent_buy_volume}
			- 최근 판매량: {recent_sell_volume}
			- 생성 시각: {current_time}
			""";

		return prompt
			.replace("{product_id}", productId.toString())
			.replace("{product_name}", productName)
			.replace("{product_description}", productDescription)
			.replace("{current_price}", currentPrice.toString())
			.replace("{previous_news}", previousNews)
			.replace("{recent_buy_volume}", totalPurchaseAmount.toString())
			.replace("{recent_sell_volume}", totalSellAmount.toString())
			.replace("{current_time}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
			;
	}

	public static String pricePromptPrefix() {
		String prefix = """
			당신은 모의 주식 시장의 가격 변동을 계산하는 금융 알고리즘 전문가입니다. 교육 목적의 모의 투자 플랫폼에 적합한 가격 변동을 생성해 주세요.
			
			## 가격 계산 지침
			1. 뉴스의 감성을 분석하여 긍정/중립/부정으로 분류하세요:
			   - 긍정적 뉴스: +3%~+10% 범위의 상승 요인
			   - 중립적 뉴스: -2%~+2% 범위의 미미한 변동 요인
			   - 부정적 뉴스: -3%~-10% 범위의 하락 요인
			
			2. 거래량 분석:
			   - 구매량 > 판매량: (구매량-판매량)/총거래량 비율에 따라 +1%~+5% 추가 상승 요인
			   - 판매량 > 구매량: (판매량-구매량)/총거래량 비율에 따라 -1%~-5% 추가 하락 요인
			   - 거의 동일한 경우: +0.5%~-0.5% 범위의 미미한 영향
			
			3. 변동성 제한:
			   - 일반적인 변동폭은 이전 가격의 -10%~+10% 범위 내로 제한
			   - 극단적인 뉴스와 거래량이 일치할 경우 최대 -15%~+15%까지 허용
			   - 계산된 가격이 이전 가격의 50% 미만이 되지 않도록 하한선 설정
			   - 계산된 가격이 이전 가격의 200%를 초과하지 않도록 상한선 설정
			
			4. 가격 계산 방법:
			   - 뉴스 영향과 거래량 영향을 모두 고려하여 최종 변동률 결정
			   - 계산식: 새 가격 = 이전 가격 × (1 + 뉴스 변동률 + 거래량 변동률)
			   - 최종 가격은 10원 단위로 반올림
			
			## 출력 형식
			<< price >> {"data": [{"id": 1, "price": 계산된_가격, "change_rate": 변동률, "news_impact": 뉴스_영향도, "volume_impact": 거래량_영향도}, {"id": 2, "price": 계산된_가격, "change_rate": 변동률, "news_impact": 뉴스_영향도, "volume_impact": 거래량_영향도}, ...]}
			""";
		return prefix;
	}

	public static String pricePrompt(
		StockProduct product,
		StockNews news, // 가장 최근 뉴스
		Integer totalPurchaseAmount,
		Integer totalSellAmount
	) {
		Integer productId = product.getId();
		String productName = product.getName();
		String productDescription = product.getDescription();
		Integer currentPrice = product.getCurrentPrice();
		String previousNews = news.getContent();

		String prompt = """
			## 입력 정보
			- 상품 ID: {product_id}
			- 상품명: {product_name}
			- 상품 설명: {product_description}
			- 이전 가격: {previous_price}
			- 최근 뉴스: {latest_news}
			- 최근 구매량: {recent_buy_volume}
			- 최근 판매량: {recent_sell_volume}
			- 생성 시각: {current_time}
			""";

		return prompt
			.replace("{product_id}", productId.toString())
			.replace("{product_name}", productName)
			.replace("{product_description}", productDescription)
			.replace("{current_price}", currentPrice.toString())
			.replace("{previous_news}", previousNews)
			.replace("{recent_buy_volume}", totalPurchaseAmount.toString())
			.replace("{recent_sell_volume}", totalSellAmount.toString())
			.replace("{current_time}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))
			;
	}
}

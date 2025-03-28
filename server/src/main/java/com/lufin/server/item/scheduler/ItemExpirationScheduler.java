package com.lufin.server.item.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.lufin.server.item.service.ItemPurchaseService;
import com.lufin.server.item.service.ItemService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemExpirationScheduler {
	private final ItemPurchaseService itemPurchaseService;
	private final ItemService itemService;

	@Scheduled(cron = "0 0 21 * * *") // 매일 오후 9시
	@Transactional
	public void expirePurchasesDaily() {
		log.info("🔔 [SCHEDULER] Starting expiration check");
		itemPurchaseService.expireItemPurchases();
		itemService.expireItems();
		log.info("✅ [SCHEDULER] expiration process completed.");
	}

}

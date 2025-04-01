package com.lufin.server.stock.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lufin.server.stock.service.StockNewsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/lufin/stocks/products/{productId}/news")
@RequiredArgsConstructor
public class StockNewsController {
	private final StockNewsService stockNewsService;

}

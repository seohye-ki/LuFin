package com.lufin.server.stock.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.lufin.server.account.domain.Account;
import com.lufin.server.account.repository.AccountRepository;
import com.lufin.server.classroom.domain.Classroom;
import com.lufin.server.classroom.repository.ClassroomRepository;
import com.lufin.server.common.constants.ErrorCode;
import com.lufin.server.common.exception.BusinessException;
import com.lufin.server.member.domain.Member;
import com.lufin.server.stock.domain.StockPortfolio;
import com.lufin.server.stock.domain.StockProduct;
import com.lufin.server.stock.domain.StockTransactionHistory;
import com.lufin.server.stock.dto.StockTransactionRequestDto;
import com.lufin.server.stock.dto.StockTransactionResponseDto;
import com.lufin.server.stock.repository.StockPortfolioRepository;
import com.lufin.server.stock.repository.StockProductRepository;
import com.lufin.server.stock.repository.StockTransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StockTransactionServiceImpl implements StockTransactionService {
	private final AccountRepository accountRepository;
	private final ClassroomRepository classroomRepository;

	private final StockProductRepository stockProductRepository;
	private final StockTransactionRepository stockTransactionRepository;
	private final StockPortfolioRepository stockPortfolioRepository;

	/**
	 * 특정 유저의 주식 거래 내역 전체 조회
	 * @param currentMember
	 */
	@Override
	public List<StockTransactionResponseDto.TransactionDetailDto> getAllTransactionByMemberId(Member currentMember,
		Integer classId) {
		log.info("특정 유저의 주식 거래 내역 전체 조회 요청: currentMember = {}", currentMember);

		if (currentMember == null) {
			log.warn("특정 유저의 주식 거래 내역 전체 조회를 요청한 멤버가 없습니다.");
			throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
		}

		try {
			List<StockTransactionResponseDto.TransactionDetailDto> transactions = stockTransactionRepository.findAllByMemberId(
				currentMember.getId(),
				classId);

			return transactions;

		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}

	}

	/**
	 * 주식 거래
	 * @param request
	 * @param currentMember
	 * @param stockProductId
	 * @param classId
	 */
	@Transactional(
		isolation = Isolation.REPEATABLE_READ,
		rollbackFor = BusinessException.class,
		timeout = 30 // 30초 안에 거래가 완료되지 않으면 타임아웃
	)
	@Override
	public StockTransactionResponseDto.TransactionInfoDto transactStock(
		StockTransactionRequestDto.TransactionInfoDto request,
		Member currentMember,
		Integer stockProductId,
		Integer classId
	) {
		log.info("주식 거래 요청: request = {}", request);

		if (request == null || currentMember == null || stockProductId == null) {
			log.warn("주식 거래에 필요한 필수 값이 누락되었습니다. request = {}, currentMember = {}, stockProductId = {}", request,
				currentMember, stockProductId);
			throw new BusinessException(ErrorCode.MISSING_REQUIRED_VALUE);
		}

		try {
			/* 접근 순서 일관화 + 비관적 락을 통해 데드락 방지 */
			// 1. 해당 주식 상품이 있는지 체크
			Optional<StockProduct> stock = stockProductRepository.findByIdWithPessimisticLock(stockProductId);

			if (!stock.isPresent()) {
				log.warn("주식 거래에 필요한 주식 상품을 찾을 수 없습니다. classId = {}, currentMember = {}", classId, currentMember);
				throw new BusinessException(ErrorCode.INVESTMENT_PRODUCT_NOT_FOUND);
			}

			StockProduct stockProduct = stock.get();

			// 2. 계좌 조회
			Optional<Account> account = accountRepository.findOpenAccountByMemberIdWithPessimisticLock(
				currentMember.getId(), classId);

			if (!account.isPresent()) {
				log.warn("주식 거래에 필요한 개인 계좌를 찾을 수 없습니다. classId = {}, currentMember = {}", classId,
					currentMember.getId());
				throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
			}

			// 3. 포트폴리오 조회
			Optional<StockPortfolio> portfolioOptional = stockPortfolioRepository.findByStockProductIdAndMemberIdWithPessimisticLock(
				stockProduct.getId(),
				currentMember.getId());

			// 4. classroom 조회
			Classroom classroom = classroomRepository.findById(classId)
				.orElseThrow(() -> new BusinessException(ErrorCode.CLASS_NOT_FOUND));

			StockTransactionResponseDto.TransactionInfoDto result = null;

			// 구매인지 판매인지 구분
			if (request.type() == 0) { // 판매
				if (!portfolioOptional.isPresent()) {
					log.warn("주식 거래에 필요한 구매 내역을 찾을 수 없습니다.");
					throw new BusinessException(ErrorCode.TRANSACTION_NOT_FOUND);
				}

				StockPortfolio portfolio = portfolioOptional.get();

				result = sellStock(
					request,
					currentMember,
					stockProduct,
					classroom,
					account,
					portfolio
				);

			} else if (request.type() == 1) { // 구매
				result = buyStock(
					request,
					currentMember,
					stockProduct,
					classroom,
					account
				);
			}

			return result;
		} catch (TransactionTimedOutException tte) {
			log.error("주식 거래 중 타임 아웃 발생: {}", tte.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

	/**
	 * 주식 구매
	 * @param request
	 * @param currentMember
	 * @param stockProduct
	 * @param classroom
	 */
	@Transactional(
		isolation = Isolation.REPEATABLE_READ,
		rollbackFor = BusinessException.class,
		timeout = 30 // 30초 안에 거래가 완료되지 않으면 타임아웃
	)
	protected StockTransactionResponseDto.TransactionInfoDto buyStock(
		StockTransactionRequestDto.TransactionInfoDto request,
		Member currentMember,
		StockProduct stockProduct,
		Classroom classroom,
		Optional<Account> account
	) {
		log.info("주식 구매 작업 시작: request = {}, currentMember = {}, stockProduct = {}, classId = {}",
			request, currentMember, stockProduct, classroom.getId());
		try {

			// 개인 계좌 객체
			Account personalAccount = account.get();

			// 구매 가능한 금액을 보유하고 있는지 체크
			if (personalAccount.getBalance() < request.totalPrice()) {
				log.warn(
					"주식 구매에 필요한 금액이 부족합니다. classId = {}, currentMember = {}, balance = {}, buyRequestTotalAmount = {}",
					classroom.getId(), currentMember, personalAccount.getBalance(), request.totalPrice());
				throw new BusinessException(ErrorCode.INSUFFICIENT_BALANCE);
			}

			/*
			 * 주식 물량이 모자랄 일 X (물량이 무한이기 때문에)
			 * 사는 동안에 가격 변동 X (실시간 가격 변동이 아니기 때문에)
			 * 사는 동안에 Balance 변동 가능성이 있지만, withdraw 메서드에서 다시 걸러줌
			 */

			StockTransactionHistory transaction = StockTransactionHistory.create(
				request.type(),
				request.quantity(),
				request.price(),
				request.totalPrice(),
				stockProduct,
				currentMember,
				classroom
			);

			// 거래 내역 저장
			StockTransactionHistory savedTransaction = stockTransactionRepository.save(transaction);
			log.info("주식 구매 작업 완료: savedTransaction = {}, 계좌 작업 개시", savedTransaction);
			// 계좌 출금
			personalAccount.withdraw(savedTransaction.getTotalPrice());
			log.info("계좌 출금 작업 완료: 잔액 = {}", personalAccount.getBalance());

			return new StockTransactionResponseDto.TransactionInfoDto(savedTransaction.getId());
		} catch (TransactionTimedOutException tte) {
			log.error("주식 구매 중 타임 아웃 발생: {}", tte.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}

	}

	/**
	 * 주식 판매
	 * @param request
	 * @param currentMember
	 * @param stockProduct
	 * @param classroom
	 */
	@Transactional(
		isolation = Isolation.REPEATABLE_READ,
		rollbackFor = BusinessException.class,
		timeout = 30 // 30초 안에 거래가 완료되지 않으면 타임아웃
	)
	protected StockTransactionResponseDto.TransactionInfoDto sellStock(
		StockTransactionRequestDto.TransactionInfoDto request,
		Member currentMember,
		StockProduct stockProduct,
		Classroom classroom,
		Optional<Account> account,
		StockPortfolio portfolio
	) {
		log.info("주식 판매 작업 시작: request = {}, currentMember = {}, stockProduct = {}, classId = {}",
			request, currentMember, stockProduct, classroom.getId());
		try {
			if (!account.isPresent()) {
				log.warn("주식 판매에 필요한 개인 계좌를 찾을 수 없습니다. classId = {}, currentMember = {}", classroom.getId(),
					currentMember);
				throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
			}

			// 개인 계좌 객체
			Account personalAccount = account.get();

			/* 판매 가능한 개수를 보유하고 있는지 확인 */

			if (request.quantity() > portfolio.getQuantity()) {
				log.warn(
					"주식 판매에 필요한 주식 개수가 부족합니다. classId = {}, currentMember = {}, sellRequestQuantity = {}, ownQuantity = {}",
					classroom.getId(), currentMember, request.quantity(), portfolio.getQuantity());
				throw new BusinessException(ErrorCode.INSUFFICIENT_STOCK_AMOUNT);
			}

			// TODO: 동시성 문제 해결 필요
			StockTransactionHistory transaction = StockTransactionHistory.create(
				request.type(),
				request.quantity(),
				request.price(),
				request.totalPrice(),
				stockProduct,
				currentMember,
				classroom
			);

			// 거래 내역 저장
			StockTransactionHistory savedTransaction = stockTransactionRepository.save(transaction);
			log.info("주식 판매 작업 완료: savedTransaction = {}, 계좌 작업 개시", savedTransaction);
			// 계좌 입금
			personalAccount.deposit(savedTransaction.getTotalPrice());
			log.info("계좌 입금 작업 완료: 잔액 = {}", personalAccount.getBalance());

			return StockTransactionResponseDto.TransactionInfoDto.stockTransactionHistoryEntityToTransactionInfoDto(
				savedTransaction);

		} catch (TransactionTimedOutException tte) {
			log.error("주식 판매 중 타임 아웃 발생: {}", tte.getMessage());
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		} catch (Exception e) {
			log.error("An error occurred: {}", e.getMessage(), e);
			throw new BusinessException(ErrorCode.SERVER_ERROR);
		}
	}

}

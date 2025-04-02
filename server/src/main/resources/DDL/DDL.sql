DROP DATABASE IF EXISTS lufin;

CREATE DATABASE lufin;

USE lufin;

-- 트리거 생성을 위한 설정 추가
SET GLOBAL log_bin_trust_function_creators = 1;
-- 권한이 없는 경우 아래 명령 사용 (관리자 인증 필요)
-- SET sql_log_bin = 0;


-- =================================
-- TABLE DEFINITIONS
-- =================================

CREATE TABLE `members`
(
    `member_id`            INT                         NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `email`                VARCHAR(255)                NOT NULL UNIQUE,
    `password`             VARCHAR(255)                NOT NULL COMMENT 'SHA-256 적용 예정',
    `secondary_password`   VARCHAR(255)                NOT NULL DEFAULT '000000' COMMENT '숫자 6자리/ SHA-256 적용 예정',
    `salt`                 VARCHAR(255)                NOT NULL,
    `member_role`          ENUM ('TEACHER', 'STUDENT') NOT NULL,
    `profile_image`        VARCHAR(255)                NULL,
    `name`                 VARCHAR(10)                 NOT NULL,
    `certification_number` INT UNIQUE,
    `created_at`           TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           TIMESTAMP                   NULL ON UPDATE CURRENT_TIMESTAMP,
    `last_login`           TIMESTAMP                            DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `activation_status`    TINYINT                     NOT NULL DEFAULT 1 COMMENT '0: 탈퇴/ 1: 활성',
    PRIMARY KEY (`member_id`),
    INDEX `idx_member_email` (`email`),
    INDEX `idx_member_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='회원 정보';

-- Classrooms table
CREATE TABLE `classrooms`
(
    `classroom_id`  INT              NOT NULL AUTO_INCREMENT,
    `teacher_id`    INT              NOT NULL,
    `code`          CHAR(5)          NOT NULL COMMENT '학생 인증용, 문자 및 숫자 혼합',
    `school`        VARCHAR(50)      NOT NULL,
    `grade`         TINYINT UNSIGNED NOT NULL,
    `class_group`   TINYINT UNSIGNED NOT NULL,
    `name`          VARCHAR(20)      NULL COMMENT '클래스 별칭이라고 봐도 무방할듯',
    `member_count`  TINYINT UNSIGNED NOT NULL DEFAULT 0,
    `thumbnail_key` VARCHAR(255)     NULL COMMENT 'PREFIX는 서버에 저장',
    `created_at`    TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`classroom_id`),
    UNIQUE KEY `uk_class_code` (`code`),
    INDEX `idx_class_teacher` (`teacher_id`),
    INDEX `idx_class_school_grade_group` (`school`, `grade`, `class_group`),
    CONSTRAINT `fk_class_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `members` (`member_id`) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='학급 정보';

-- Member-Class relationship
CREATE TABLE `member_classrooms`
(
    `member_class_id` INT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `classroom_id`    INT       NOT NULL COMMENT '멤버-클래스 간 중간테이블',
    `member_id`       INT       NOT NULL,
    `join_date`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '해당 반에 JOIN 된 일자 확인',
    `is_current`      BOOLEAN   NOT NULL DEFAULT TRUE,
    PRIMARY KEY (`member_class_id`),
    INDEX `idx_memberclass_class` (`classroom_id`),
    INDEX `idx_memberclass_member` (`member_id`),
    INDEX `idx_memberclass_current` (`is_current`),
    INDEX `idx_class_join` (`classroom_id`, `join_date`),
    UNIQUE KEY `uk_member_class_current` (`member_id`, `classroom_id`, `is_current`),
    CONSTRAINT `fk_memberclass_class` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_memberclass_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='회원-학급 관계';

-- Accounts table
CREATE TABLE `accounts`
(
    `account_id`     INT         NOT NULL AUTO_INCREMENT COMMENT '학년이 변경 될 때마다 새 계좌 지급',
    `member_id`      INT         NULL COMMENT '계좌 주인',
    `classroom_id`   INT         NULL COMMENT '소속 학급 ID',
    `account_number` VARCHAR(15) NOT NULL UNIQUE,
    `balance`        INT         NOT NULL DEFAULT 0,
    `created_at`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `closed_at`      TIMESTAMP   NULL,
    PRIMARY KEY (`account_id`),
    INDEX `idx_account_member` (`member_id`),
    INDEX `idx_account_number` (`account_number`),
    CONSTRAINT `fk_account_class` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_account_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='계좌 정보';

-- Transaction histories
CREATE TABLE `transaction_histories`
(
    `transaction_id`     INT                                        NOT NULL AUTO_INCREMENT,
    `from_account_id`    INT                                        NOT NULL COMMENT '누구의 계좌에서 이체했는가',
    `to_account_number`  INT                                        NOT NULL,
    `executor_member_id` INT                                        NOT NULL COMMENT '누가 이체를 했는가',
    `transaction_type`   ENUM ('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') NOT NULL COMMENT '입금, 출금, 이체',
    `amount`             INT                                        NOT NULL,
    `balance_after`      INT                                        NULL COMMENT '거래 후 잔액 (교사 지급 시 NULL)',
    `created_at`         TIMESTAMP                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `auto_transfer_id`   INT                                        NULL COMMENT '자동 이체 ID',
    `description`        TEXT                                       NULL,
    `status`             ENUM ('SUCCESS', 'FAILED', 'PENDING')      NOT NULL DEFAULT 'PENDING' COMMENT '성공, 실패, 대기',
    `executor_role`      TINYINT                                    NOT NULL COMMENT '0: 교사, 1: 학생',
    PRIMARY KEY (`transaction_id`),
    INDEX `idx_transaction_from` (`from_account_id`),
    INDEX `idx_transaction_to` (`to_account_number`),
    INDEX `idx_transaction_executor` (`executor_member_id`),
    INDEX `idx_transaction_auto` (`auto_transfer_id`),
    INDEX `idx_transaction_date` (`created_at`),
    INDEX `idx_transaction_status` (`status`),
    CONSTRAINT `fk_transaction_from` FOREIGN KEY (`from_account_id`) REFERENCES `accounts` (`account_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_transaction_executor` FOREIGN KEY (`executor_member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='거래 내역';

-- Auto transfers
CREATE TABLE `auto_transfers`
(
    `auto_transfer_id`  INT       NOT NULL AUTO_INCREMENT COMMENT '자동 이체 설정 관련 테이블',
    `member_id`         INT       NOT NULL COMMENT '자동 이체 설정 한 사람 (FK)',
    `from_account_id`   INT       NOT NULL COMMENT '보내는 계좌 ID (FK)',
    `to_account_number` INT       NOT NULL,
    `amount`            INT       NOT NULL,
    `day_of_week`       TINYINT   NULL COMMENT '1(일), 2(월), 4(화), 8(수), 16(목), 32(금), 64(토)',
    `is_active`         BOOLEAN   NOT NULL COMMENT 'T: 자동 이체 활성화 / F: 비활성화',
    `end_at`            TIMESTAMP NOT NULL,
    `created_at`        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`        TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`auto_transfer_id`),
    INDEX `idx_autotransfer_member` (`member_id`),
    INDEX `idx_autotransfer_account` (`from_account_id`),
    INDEX `idx_autotransfer_active` (`is_active`),
    CONSTRAINT `fk_autotransfer_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_autotransfer_account` FOREIGN KEY (`from_account_id`) REFERENCES `accounts` (`account_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='자동이체 정보';

-- credit_scores
CREATE TABLE credit_scores
(
    member_id                 INT PRIMARY KEY,
    score                     TINYINT     NOT NULL DEFAULT 50,
    grade                     VARCHAR(10) NOT NULL,
    credit_status             TINYINT     NOT NULL DEFAULT 0 COMMENT '0: 정상/ 1: 신용불량자',
    credit_status_description TEXT        NULL COMMENT '신용불량자에서 회생할 경우 교사가 사유를 입력 함',
    updated_at                DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_creditscore_member FOREIGN KEY (member_id)
        REFERENCES members (member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='신용 등급 현재 상태';

-- Credit score histories
CREATE TABLE credit_score_histories
(
    history_id   INT AUTO_INCREMENT PRIMARY KEY,
    member_id    INT          NOT NULL,
    score_change TINYINT      NOT NULL,
    reason       VARCHAR(100) NOT NULL,
    created_at   DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_credithistory_member FOREIGN KEY (member_id)
        REFERENCES members (member_id) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='신용 점수 이력';

-- Savings products
CREATE TABLE `savings_products`
(
    `savings_product_id` INT           NOT NULL AUTO_INCREMENT COMMENT '적금 상품 고유 번호',
    `name`               VARCHAR(30)   NOT NULL COMMENT '적금 상품 이름',
    `description`        TEXT          NULL COMMENT '적금 상품 설명',
    `period`             INT           NOT NULL COMMENT '적금이 유지되는 기간 (일수)',
    `interest_rate`      DECIMAL(5, 2) NOT NULL COMMENT '이자율(소수점 포함가능)',
    `min_amount`         INT           NOT NULL COMMENT '적금 납입에 필요한 최소 금액',
    `created_at`         TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일자',
    `updated_at`         TIMESTAMP     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '변경 일자',
    PRIMARY KEY (`savings_product_id`),
    INDEX `idx_savingsproduct_interestrate` (`interest_rate`),
    INDEX `idx_savingsproduct_period` (`period`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='적금 상품';

-- Savings accounts
CREATE TABLE `savings_accounts`
(
    `savings_account_id` INT       NOT NULL AUTO_INCREMENT COMMENT '적금 계좌 고유 번호',
    `member_id`          INT       NOT NULL COMMENT '적금 신청 학생 고유 번호',
    `savings_product_id` INT       NOT NULL COMMENT '적금 상품 고유 번호',
    `status`             TINYINT   NOT NULL COMMENT '만기: 0, 활성: 1',
    `current_balance`    INT       NOT NULL DEFAULT 0 COMMENT '현재 계좌에 있는 잔액. 21억이 넘지 않을 것이라 예상',
    `deposit_count`      TINYINT   NOT NULL DEFAULT 0 COMMENT '계좌 변경일과 다른 날일 경우에만 count 상승(현재 잔액 변동)',
    `created_at`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '계좌 생성일',
    `updated_at`         TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '계좌 상태 update 될 때마다',
    `due_date`           TIMESTAMP NOT NULL COMMENT '개설일 + 적금 유지 기간으로 계산',
    PRIMARY KEY (`savings_account_id`),
    INDEX `idx_savingsaccount_member` (`member_id`),
    INDEX `idx_savingsaccount_product` (`savings_product_id`),
    INDEX `idx_savingsaccount_status` (`status`),
    INDEX `idx_savingsaccount_duedate` (`due_date`),
    CONSTRAINT `fk_savingsaccount_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_savingsaccount_product` FOREIGN KEY (`savings_product_id`) REFERENCES `savings_products` (`savings_product_id`) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='적금 계좌';

-- Savings histories
CREATE TABLE `savings_histories`
(
    `savings_id`         INT       NOT NULL AUTO_INCREMENT COMMENT '각 적금 거래에 대한 고유 번호',
    `savings_account_id` INT       NOT NULL COMMENT '적금 계좌 고유 번호',
    `type`               TINYINT   NOT NULL COMMENT '출금: 0, 입금: 1(계좌 오픈할 때도 입금해야 됨)',
    `amount`             INT       NOT NULL COMMENT '적금에 넣은 금액',
    `created_at`         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '적금 거래 내역 생성 일자',
    `updated_at`         TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '적금 거래 내역 변경 일자',
    PRIMARY KEY (`savings_id`),
    INDEX `idx_savingshistory_account` (`savings_account_id`),
    INDEX `idx_savingshistory_type` (`type`),
    INDEX `idx_savingshistory_date` (`created_at`),
    CONSTRAINT `fk_savingshistory_account` FOREIGN KEY (`savings_account_id`) REFERENCES `savings_accounts` (`savings_account_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='적금 거래 내역';

-- Loan products
CREATE TABLE `loan_products`
(
    `loan_product_id` INT           NOT NULL AUTO_INCREMENT COMMENT '대출 상품 고유 번호',
    `name`            VARCHAR(30)   NOT NULL COMMENT '대출 상품 이름',
    `description`     TEXT          NOT NULL COMMENT '대출 상품 설명',
    `credit_rank`     TINYINT       NOT NULL COMMENT '0(A)~4(F)',
    `max_amount`      INT           NOT NULL COMMENT '대출 가능한 최대 액수',
    `interest_rate`   DECIMAL(5, 2) NOT NULL COMMENT '이자율(소수점 포함가능)',
    `period`          TINYINT       NOT NULL COMMENT '대출 상환 기간(일단위)',
    `created_at`      TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '상품 생성 일자',
    `updated_at`      TIMESTAMP     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '상품 변경 일자',
    PRIMARY KEY (`loan_product_id`),
    INDEX `idx_loanproduct_creditrank` (`credit_rank`),
    INDEX `idx_loanproduct_interestrate` (`interest_rate`),
    INDEX `idx_loanproduct_period` (`period`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='대출 상품';

-- Loan applications
CREATE TABLE `loan_applications`
(
    `loan_application_id` INT                                                               NOT NULL AUTO_INCREMENT COMMENT '대출 관리 고유 번호',
    `member_id`           INT                                                               NOT NULL COMMENT '대출 신청한 학생 고유 번호',
    `classroom_id`        INT                                                               NOT NULL COMMENT '대출 신청한 학급 고유 번호',
    `loan_product_id`     INT                                                               NOT NULL COMMENT '대출 상품 고유 번호',
    `description`         TEXT                                                              NOT NULL COMMENT '대출 신청 사유',
    `status`              ENUM ('PENDING','APPROVED','REJECTED','OPEN','OVERDUED','CLOSED') NOT NULL COMMENT '대기,승인,거절,활성,연체,상환완료',
    `required_amount`     INT                                                               NOT NULL COMMENT '원금, 지급 해야할 금액',
    `interest_amount`     INT                                                               NOT NULL COMMENT '이자액, 원금 * 이자율로 계산',
    `overdue_count`       TINYINT UNSIGNED                                                  NULL     DEFAULT 0 COMMENT '연체 횟수',
    `next_payment_date`   TIMESTAMP                                                         NULL COMMENT '다음 이자 상환일, 7일 주기',
    `created_at`          TIMESTAMP                                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '대출 관리 생성 일자',
    `started_at`          TIMESTAMP                                                         NULL COMMENT '대출 시작 일자(대금 지급 일자)',
    `due_date`            TIMESTAMP                                                         NULL COMMENT '대출 만기 일자(상환 예정 일자)',
    PRIMARY KEY (`loan_application_id`),
    INDEX `idx_loanapp_member` (`member_id`),
    INDEX `idx_loanapp_product` (`loan_product_id`),
    INDEX `idx_loanapp_status` (`status`),
    INDEX `idx_loanapp_nextpayment` (`next_payment_date`),
    INDEX `idx_loanapp_duedate` (`due_date`),
    CONSTRAINT `fk_loanapp_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_loanapp_classroom` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_loanapp_product` FOREIGN KEY (`loan_product_id`) REFERENCES `loan_products` (`loan_product_id`) ON DELETE RESTRICT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='대출 신청';

-- Loan histories
CREATE TABLE `loan_histories`
(
    `loan_id`             INT                         NOT NULL AUTO_INCREMENT COMMENT '대출 고유 번호',
    `loan_application_id` INT                         NOT NULL COMMENT '대출 관리 고유 번호',
    `type`                ENUM ('DEPOSIT','WITHDRAW') NOT NULL COMMENT '입금(대금 지급), 출금(상환)',
    `amount`              INT                         NOT NULL COMMENT '금액',
    `created_at`          TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '내역 생성 일자',
    `updated_at`          TIMESTAMP                   NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '내역 변경 일자',
    PRIMARY KEY (`loan_id`),
    INDEX `idx_loanhistory_application` (`loan_application_id`),
    INDEX `idx_loanhistory_type` (`type`),
    INDEX `idx_loanhistory_date` (`created_at`),
    CONSTRAINT `fk_loanhistory_application` FOREIGN KEY (`loan_application_id`) REFERENCES `loan_applications` (`loan_application_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='대출 내역';

-- Stock products
CREATE TABLE `stock_products`
(
    `stock_product_id` INT         NOT NULL AUTO_INCREMENT COMMENT '투자 상품 고유 번호',
    `name`             VARCHAR(50) NOT NULL COMMENT '투자 유형',
    `description`      TEXT        NOT NULL COMMENT '투자 상품 설명',
    `initial_price`    INT         NOT NULL COMMENT '최초 공시 가격',
    `current_price`    INT         NOT NULL COMMENT '현재 가격',
    `created_at`       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일자',
    `updated_at`       TIMESTAMP   NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '변경 일자',
    PRIMARY KEY (`stock_product_id`),
    INDEX `idx_stockproduct_name` (`name`),
    INDEX `idx_stockproduct_price` (`current_price`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주식 상품';

-- Stock price histories
CREATE TABLE `stock_price_histories`
(
    `stock_price_id`   INT       NOT NULL AUTO_INCREMENT COMMENT '가격 변동 기록 고유 번호',
    `stock_product_id` INT       NOT NULL COMMENT '투자 상품 고유 번호',
    `unit_price`       INT       NOT NULL COMMENT '공시 당시 단가',
    `created_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일자(공시 일자)',
    `updated_at`       TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '변경 일자',
    PRIMARY KEY (`stock_price_id`),
    INDEX `idx_stockprice_product` (`stock_product_id`),
    INDEX `idx_stockprice_date` (`created_at`),
    CONSTRAINT `fk_stockprice_product` FOREIGN KEY (`stock_product_id`) REFERENCES `stock_products` (`stock_product_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주식 가격 변동 내역';

-- Stock portfolios
CREATE TABLE `stock_portfolios`
(
    `stock_portfolio_id`    INT       NOT NULL AUTO_INCREMENT COMMENT '포트폴리오 고유 번호',
    `stock_product_id`      INT       NOT NULL COMMENT '투자 상품 고유 번호',
    `member_id`             INT       NOT NULL COMMENT '학생 고유 번호',
    `quantity`              INT       NOT NULL COMMENT '한 투자 상품에 대한 총 보유 수량',
    `total_purchase_amount` INT       NOT NULL COMMENT '한 투자 상품에 대한 총 매수 금액',
    `total_sell_amount`     INT       NOT NULL COMMENT '한 투자 상품에 대한 총 매도 금액',
    `created_at`            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`            TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '최종 거래 일자',
    PRIMARY KEY (`stock_portfolio_id`),
    UNIQUE KEY `uk_portfolio_member_product` (`member_id`, `stock_product_id`),
    INDEX `idx_portfolio_product` (`stock_product_id`),
    INDEX `idx_portfolio_member` (`member_id`),
    CONSTRAINT `fk_portfolio_product` FOREIGN KEY (`stock_product_id`) REFERENCES `stock_products` (`stock_product_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_portfolio_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주식 포트폴리오';

-- Stock histories
CREATE TABLE `stock_histories`
(
    `stock_history_id` INT       NOT NULL AUTO_INCREMENT COMMENT '투자 내역 고유 번호',
    `stock_product_id` INT       NOT NULL COMMENT '투자 상품 고유 번호',
    `classroom_id`     INT       NOT NULL COMMENT '',
    `member_id`        INT       NOT NULL COMMENT 'PK',
    `type`             TINYINT   NOT NULL COMMENT '판매: 0, 구매: 1',
    `quantity`         INT       NOT NULL COMMENT '주식 수량',
    `unit_price`       INT       NOT NULL COMMENT '거래 당시 단가',
    `total_value`      INT       NOT NULL COMMENT '거래 당시 총 가치',
    `created_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일자',
    `updated_at`       TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '변경 일자',
    PRIMARY KEY (`stock_history_id`),
    INDEX `idx_stockhistory_product` (`stock_product_id`),
    INDEX `idx_stockhistory_member` (`member_id`),
    INDEX `idx_stockhistory_type` (`type`),
    INDEX `idx_stockhistory_date` (`created_at`),
    CONSTRAINT `fk_stockhistory_product` FOREIGN KEY (`stock_product_id`) REFERENCES `stock_products` (`stock_product_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_stockhistory_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_stockhistory_classroom` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주식 거래 내역';


-- Item templates
CREATE TABLE `item_templates`
(
    `item_template_id` INT         NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(50) NOT NULL,
    PRIMARY KEY (`item_template_id`),
    UNIQUE KEY `uk_itemtemplate_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='아이템 템플릿';

-- Items
CREATE TABLE `items`
(
    `item_id`            INT              NOT NULL AUTO_INCREMENT,
    `classroom_id`       INT              NOT NULL,
    `name`               VARCHAR(50)      NOT NULL,
    `type`               TINYINT          NOT NULL DEFAULT 1 COMMENT '0: 바로 사용 가능, 1: 요청 필요',
    `price`              TINYINT UNSIGNED NOT NULL DEFAULT 0,
    `quantity_available` TINYINT UNSIGNED NOT NULL DEFAULT 0,
    `quantity_sold`      TINYINT UNSIGNED NOT NULL DEFAULT 0,
    `status`             BOOLEAN          NOT NULL DEFAULT TRUE COMMENT 'T: 판매중 / F: 판매 중단',
    `expiration_date`    TIMESTAMP        NOT NULL COMMENT '최대 1년 (해당 학년 종료)',
    `created_at`         TIMESTAMP        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`         TIMESTAMP        NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`item_id`),
    INDEX `idx_item_class` (`classroom_id`),
    INDEX `idx_item_name` (`name`),
    INDEX `idx_item_status` (`status`),
    INDEX `idx_item_expire` (`expiration_date`),
    CONSTRAINT `fk_item_class` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='아이템 정보';

-- 순서 변경: item_purchases가 item_requests 전에 생성되도록 함
CREATE TABLE `item_purchases`
(
    `item_purchase_id` INT                                                  NOT NULL AUTO_INCREMENT,
    `member_id`        INT                                                  NOT NULL,
    `item_id`          INT                                                  NOT NULL,
    `item_count`       INT                                                  NOT NULL DEFAULT 1,
    `purchase_price`   INT                                                  NOT NULL,
    `status`           ENUM ('BUY', 'REFUND', 'USED', 'EXPIRED', 'PENDING') NOT NULL DEFAULT 'BUY',
    `created_at`       TIMESTAMP                                            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP                                            NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`item_purchase_id`),
    INDEX `idx_itempurchase_member` (`member_id`),
    INDEX `idx_itempurchase_item` (`item_id`),
    INDEX `idx_itempurchase_status` (`status`),
    CONSTRAINT `fk_itempurchase_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_itempurchase_item` FOREIGN KEY (`item_id`) REFERENCES `items` (`item_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='아이템 구매';

-- Item requests
CREATE TABLE `item_requests`
(
    `item_request_id`  INT                                      NOT NULL AUTO_INCREMENT,
    `requester_id`     INT                                      NOT NULL,
    `approved_by`      INT                                      NULL,
    `item_purchase_id` INT                                      NOT NULL,
    `status`           ENUM ('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT '대기, 승인, 반려',
    `created_at`       TIMESTAMP                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP                                NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`item_request_id`),
    INDEX `idx_itemrequest_requester` (`requester_id`),
    INDEX `idx_itemrequest_approver` (`approved_by`),
    INDEX `idx_itemrequest_purchase` (`item_purchase_id`),
    INDEX `idx_itemrequest_status` (`status`),
    CONSTRAINT `fk_itemrequest_requester` FOREIGN KEY (`requester_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_itemrequest_approver` FOREIGN KEY (`approved_by`) REFERENCES `members` (`member_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_itemrequest_purchase` FOREIGN KEY (`item_purchase_id`) REFERENCES `item_purchases` (`item_purchase_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='아이템 사용 요청';

-- Notifications
CREATE TABLE `notifications`
(
    `notification_id`     INT       NOT NULL AUTO_INCREMENT,
    `member_id`           INT       NOT NULL,
    `transaction_id`      INT       NULL,
    `loan_application_id` INT       NULL COMMENT '대출 관리 고유 번호',
    `stock_portfolio_id`  INT       NULL COMMENT '포트폴리오 고유 번호',
    `message`             TEXT      NOT NULL,
    `priority`            TINYINT   NULL     DEFAULT 1 COMMENT '하(0), 중(1), 상(2)',
    `is_read`             BOOLEAN   NOT NULL DEFAULT FALSE,
    `created_at`          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`          TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`notification_id`),
    INDEX `idx_notification_member` (`member_id`),
    INDEX `idx_notification_transaction` (`transaction_id`),
    INDEX `idx_notification_loan` (`loan_application_id`),
    INDEX `idx_notification_portfolio` (`stock_portfolio_id`),
    INDEX `idx_notification_read` (`is_read`),
    INDEX `idx_notification_priority` (`priority`),
    CONSTRAINT `fk_notification_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_notification_transaction` FOREIGN KEY (`transaction_id`) REFERENCES `transaction_histories` (`transaction_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_notification_loan` FOREIGN KEY (`loan_application_id`) REFERENCES `loan_applications` (`loan_application_id`) ON DELETE SET NULL,
    CONSTRAINT `fk_notification_portfolio` FOREIGN KEY (`stock_portfolio_id`) REFERENCES `stock_portfolios` (`stock_portfolio_id`) ON DELETE SET NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='알림';

-- Stock news
CREATE TABLE `stock_news`
(
    `stock_news_id`    INT       NOT NULL AUTO_INCREMENT COMMENT '공시 정보 고유 번호',
    `stock_product_id` INT       NOT NULL COMMENT '투자 상품 고유 번호',
    `content`          TEXT      NOT NULL COMMENT '공시 정보 내용',
    `created_at`       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 일자(정보 공시 일자)',
    `updated_at`       TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '변경 일자',
    PRIMARY KEY (`stock_news_id`),
    INDEX `idx_stocknews_product` (`stock_product_id`),
    INDEX `idx_stocknews_date` (`created_at`),
    CONSTRAINT `fk_stocknews_product` FOREIGN KEY (`stock_product_id`) REFERENCES `stock_products` (`stock_product_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='주식 뉴스';

-- Jobs
CREATE TABLE `jobs`
(
    `job_id`         INT         NOT NULL AUTO_INCREMENT,
    `classroom_id`   INT         NOT NULL,
    `name`           VARCHAR(20) NOT NULL,
    `description`    TEXT        NOT NULL,
    `max_worker`     INT         NOT NULL DEFAULT 1,
    `current_worker` INT         NOT NULL DEFAULT 0,
    `wage`           INT         NOT NULL DEFAULT 0,
    `created_at`     TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`job_id`),
    INDEX `idx_job_name` (`name`),
    INDEX `idx_job_class` (`classroom_id`),
    CONSTRAINT `fk_job_class` FOREIGN KEY (`classroom_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='직업 정보';

-- Job requests
CREATE TABLE `job_requests`
(
    `job_request_id` INT                                    NOT NULL AUTO_INCREMENT,
    `job_id`         INT                                    NOT NULL,
    `member_id`      INT                                    NOT NULL,
    `type`           ENUM ('APPLICATION','RESIGNATION')     NOT NULL COMMENT '지원/퇴사',
    `message`        TEXT                                   NOT NULL COMMENT '지원/퇴사 시 요청 멘트',
    `status`         ENUM ('PENDING','APPROVED','REJECTED') NOT NULL DEFAULT 'PENDING' COMMENT '검토중/수락/거절',
    `created_at`     TIMESTAMP                              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`     TIMESTAMP                              NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`job_request_id`),
    INDEX `idx_jobrequest_job` (`job_id`),
    INDEX `idx_jobrequest_member` (`member_id`),
    INDEX `idx_jobrequest_status` (`status`),
    CONSTRAINT `fk_jobrequest_job` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`job_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_jobrequest_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='직업 신청';

-- Missions
CREATE TABLE `missions`
(
    `mission_id`           INT                                        NOT NULL AUTO_INCREMENT,
    `class_id`             INT                                        NOT NULL,
    `title`                VARCHAR(100)                               NOT NULL,
    `content`              TEXT                                       NOT NULL,
    `difficulty`           TINYINT                                    NOT NULL DEFAULT 2 COMMENT '상,중,하',
    `max_participants`     TINYINT UNSIGNED                           NOT NULL DEFAULT 1 COMMENT '참여할 수 있는 최대인원',
    `current_participants` TINYINT UNSIGNED                           NOT NULL DEFAULT 0 COMMENT '현재 참여신청 인원',
    `wage`                 INT                                        NOT NULL DEFAULT 0,
    `mission_date`         TIMESTAMP                                  NOT NULL DEFAULT (CURRENT_TIMESTAMP + INTERVAL 2 DAY) COMMENT '모집기간이후로만 설정가능',
    `status`               ENUM ('RECRUITING','IN_PROGRESS','CLOSED') NOT NULL DEFAULT 'RECRUITING' COMMENT '모집중/진행중/종료',
    `created_at`           TIMESTAMP                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`           TIMESTAMP                                  NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`mission_id`),
    INDEX `idx_mission_class` (`class_id`),
    INDEX `idx_mission_status` (`status`),
    INDEX `idx_mission_date` (`mission_date`),
    CONSTRAINT `fk_mission_class` FOREIGN KEY (`class_id`) REFERENCES `classrooms` (`classroom_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='미션 정보';

-- Mission images
CREATE TABLE `mission_images`
(
    `mission_image_id` INT           NOT NULL AUTO_INCREMENT,
    `mission_id`       INT           NOT NULL,
    `object_key`       VARCHAR(1024) NOT NULL COMMENT 'S3 object key',
    `created_at`       TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'url 생성일',
    `updated_at`       TIMESTAMP     NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'url 업데이트 일',
    PRIMARY KEY (`mission_image_id`),
    INDEX `idx_missionimage_mission` (`mission_id`),
    CONSTRAINT `fk_missionimage_mission` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`mission_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='미션 이미지';

-- Mission participations
CREATE TABLE `mission_participations`
(
    `participation_id` INT                                                           NOT NULL AUTO_INCREMENT,
    `mission_id`       INT                                                           NOT NULL,
    `member_id`        INT                                                           NOT NULL,
    `status`           ENUM ('IN_PROGRESS','CHECKING','SUCCESS','FAILED','REJECTED') NOT NULL DEFAULT 'IN_PROGRESS' COMMENT '진행중/검토중/성공/실패/보류(재요청 필요)',
    `wage_status`      BOOLEAN                                                       NOT NULL DEFAULT false COMMENT '0: 지급안함 / 1: 지급함',
    `reject_count`     TINYINT UNSIGNED                                              NOT NULL DEFAULT 0 COMMENT '미션을 제대로 하지 않아서 보류시킨 횟수',
    `created_at`       TIMESTAMP                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at`       TIMESTAMP                                                     NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`participation_id`),
    UNIQUE KEY `uk_participation` (`mission_id`, `member_id`),
    INDEX `idx_participation_mission` (`mission_id`),
    INDEX `idx_participation_member` (`member_id`),
    INDEX `idx_participation_status` (`status`),
    INDEX `idx_participation_wage` (`wage_status`),
    CONSTRAINT `fk_participation_mission` FOREIGN KEY (`mission_id`) REFERENCES `missions` (`mission_id`) ON DELETE CASCADE,
    CONSTRAINT `fk_participation_member` FOREIGN KEY (`member_id`) REFERENCES `members` (`member_id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='미션 참여';


-- =================================
-- DATABASE TRIGGERS
-- =================================

DELIMITER //

-- 미션 참여가 추가될 때 참가자 수를 업데이트하고 초과 신청을 관리하는 트리거
-- 선착순 미션 참가에서 동시성 제어와 최대 참가자 수 제한을 위해 필수적인 트리거
CREATE TRIGGER after_mission_participation_insert
    AFTER INSERT
    ON mission_participations
    FOR EACH ROW
BEGIN
    -- 미션 참가자 수 증가
    UPDATE missions
    SET current_participants = current_participants + 1
    WHERE mission_id = NEW.mission_id;

    -- 참가자 수가 초과되었는지 확인하고 처리
    IF ((SELECT current_participants FROM missions WHERE mission_id = NEW.mission_id) >
        (SELECT max_participants FROM missions WHERE mission_id = NEW.mission_id)) THEN
        -- 초과된 경우 상태를 거부로 변경
        UPDATE mission_participations
        SET status = 'REJECTED'
        WHERE participation_id = NEW.participation_id;
    END IF;
END //

-- 투자 내역이 추가될 때 포트폴리오를 자동으로 업데이트하는 트리거
CREATE TRIGGER after_stock_history_insert
    AFTER INSERT
    ON stock_histories
    FOR EACH ROW
BEGIN
    DECLARE portfolio_exists INT;

    -- 해당 회원의 해당 주식 포트폴리오가 존재하는지 확인
    SELECT COUNT(*)
    INTO portfolio_exists
    FROM stock_portfolios
    WHERE member_id = NEW.member_id
      AND stock_product_id = NEW.stock_product_id;

    IF portfolio_exists = 0 THEN
        -- 포트폴리오가 없을 경우 새로 생성
        IF NEW.type = 1 THEN -- 구매인 경우
            INSERT INTO stock_portfolios (stock_product_id,
                                          member_id,
                                          quantity,
                                          total_purchase_amount,
                                          total_sell_amount,
                                          created_at)
            VALUES (NEW.stock_product_id,
                    NEW.member_id,
                    NEW.quantity,
                    NEW.total_value,
                    0,
                    NOW());
        END IF;
    ELSE
        -- 포트폴리오가 있을 경우 업데이트
        IF NEW.type = 1 THEN -- 구매인 경우
            UPDATE stock_portfolios
            SET quantity              = quantity + NEW.quantity,
                total_purchase_amount = total_purchase_amount + NEW.total_value,
                updated_at            = NOW()
            WHERE stock_product_id = NEW.stock_product_id
              AND member_id = NEW.member_id;
        ELSE -- 판매인 경우
            UPDATE stock_portfolios
            SET quantity          = quantity - NEW.quantity,
                total_sell_amount = total_sell_amount + NEW.total_value,
                updated_at        = NOW()
            WHERE stock_product_id = NEW.stock_product_id
              AND member_id = NEW.member_id;
        END IF;
    END IF;
END //

DELIMITER ;

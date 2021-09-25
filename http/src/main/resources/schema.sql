
CREATE TABLE `PRODUCT`
(
    `product_id`             BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `title`                  VARCHAR(500) NOT NULL,
    `total_investing_amount` BIGINT(20) UNSIGNED NOT NULL,
    `started_at`             DATETIME     NOT NULL,
    `finished_at`            DATETIME     NOT NULL,
    `reg_date`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mod_date`               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (product_id)
);
CREATE INDEX PRODUCT_started_finished_at_IDX ON PRODUCT (started_at,finished_at);


CREATE TABLE `PRODUCT_INVEST_LOG`
(
    `log_id`        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`       VARCHAR(100) NOT NULL,
    `invest_amount` BIGINT(20) UNSIGNED NOT NULL,
    `product_id`    BIGINT(20) UNSIGNED NOT NULL,
    `invest_result` ENUM('SUCCESS','FAIL') NOT NULL,
    `fail_reason`   VARCHAR(100),
    `accrue_user_invest`    BIGINT(20) UNSIGNED NOT NULL,
    `accrue_product_invest` BIGINT(20) UNSIGNED NOT NULL,
    `reg_date`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mod_date`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (log_id)
);
CREATE INDEX PRODUCT_INVEST_LOG_user_id_IDX ON PRODUCT_INVEST_LOG (user_id, invest_result);


CREATE TABLE `PRODUCT_INVEST_STATUS`
(
    `status_id`        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `product_id`       BIGINT(20) UNSIGNED NOT NULL,
    `invested_amount`  BIGINT(20) UNSIGNED NOT NULL DEFAULT 0,
    `investing_cnt`    BIGINT(20) UNSIGNED NOT NULL DEFAULT 0,
    `investing_status` ENUM('ONGOING', 'COMPLETE') NOT NULL DEFAULT 'ONGOING',
    `reg_date`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mod_date`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (status_id)
);
CREATE UNIQUE INDEX PRODUCT_INVEST_STATUS_product_id_IDX ON PRODUCT_INVEST_STATUS (product_id);




INSERT INTO PRODUCT (reg_date, mod_date, title, total_investing_amount, started_at, finished_at)
VALUES (now(), now(), 'product1', 100, '2021-09-21', '2021-09-25');

INSERT INTO PRODUCT_INVEST_STATUS (reg_date, mod_date, product_id, invested_amount, investing_cnt,
                                   investing_status)
VALUES (NOW(), NOW(), 1, 0, 0, 'ONGOING');



INSERT INTO PRODUCT_INVEST_LOG (reg_date, mod_date, user_id, invest_amount, product_id, invest_result,
                                fail_reason, accrue_user_invest, accrue_product_invest)
VALUES (NOW(), NOW(), 'userId', 100, 1, 'SUCCESS', NULL, 100, 100);
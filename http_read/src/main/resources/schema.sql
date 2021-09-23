
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

CREATE TABLE `PRODUCT_INVEST_LOG`
(
    `log_id`        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `user_id`       VARCHAR(100) NOT NULL,
    `invest_amount` BIGINT(20) UNSIGNED NOT NULL,
    `product_id`    BIGINT(20) UNSIGNED NOT NULL,
    `reg_date`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mod_date`      DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (log_id)
);

CREATE TABLE `PRODUCT_INVEST_STATUS`
(
    `status_id`        BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    `product_id`       BIGINT(20) UNSIGNED NOT NULL,
    `invested_amount`  BIGINT(20) UNSIGNED NOT NULL,
    `investing_status` ENUM('ONGOING', 'COMPLETE') NOT NULL DEFAULT 'ONGOING',
    `reg_date`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `mod_date`         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (status_id)
);



INSERT INTO PRODUCT (reg_date, mod_date, title, total_investing_amount, started_at, finished_at)
VALUES (now(), now(), 'product1', 100, '2021-09-21', '2021-09-25');

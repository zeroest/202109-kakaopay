package me.zeroest.kyd_kakaopay.repository;

import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInvestLogRepository extends JpaRepository<ProductInvestLog, Long> {
}

package me.zeroest.kyd_kakaopay.repository;

import me.zeroest.kyd_kakaopay.domain.product.ProductInvestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInvestStatusRepository extends JpaRepository<ProductInvestStatus, Long> {
}

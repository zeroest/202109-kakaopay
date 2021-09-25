package me.zeroest.kyd_kakaopay.repository.invest.status;

import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductInvestStatusRepository extends JpaRepository<ProductInvestStatus, Long> {

    @EntityGraph(attributePaths = "product")
    Optional<ProductInvestStatus> findByProductId(Long productId);

}

package me.zeroest.kyd_kakaopay.repository.product;

import me.zeroest.kyd_kakaopay.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
}

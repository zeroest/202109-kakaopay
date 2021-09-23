package me.zeroest.kyd_kakaopay.repository.product;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface ProductRepositoryCustom {

    QueryResults<Product> findProductAll(LocalDateTime now, Pageable page);

}

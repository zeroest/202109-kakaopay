package me.zeroest.kyd_kakaopay.repository.product;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.time.LocalDateTime;

import static me.zeroest.kyd_kakaopay.domain.product.QProduct.product;

public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public QueryResults<Product> findProductAll(LocalDateTime now, Pageable page) {

        return queryFactory.select(product)
                .from(product)
                .where(
                        product.startedAt.before(now)
                        , product.finishedAt.after(now)
                )
                .orderBy(product.id.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetchResults();

    }
}

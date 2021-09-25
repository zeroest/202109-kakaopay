package me.zeroest.kyd_kakaopay.repository.invest.user;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import me.zeroest.kyd_kakaopay.dto.product.QMyInvestDto;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import static me.zeroest.kyd_kakaopay.domain.invest.user.QProductInvestUser.productInvestUser;
import static me.zeroest.kyd_kakaopay.domain.product.QProduct.product;

public class ProductInvestUserRepositoryImpl implements ProductInvestUserRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductInvestUserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public QueryResults<MyInvestDto> findMyInvest(String userId, Pageable page) {

        return queryFactory.select(new QMyInvestDto(
                product.id, productInvestUser.id.userId, product.title, product.totalAmount,
                        productInvestUser.totalInvestAmount, productInvestUser.lastModifiedDate
                ))
                .from(productInvestUser)
                .innerJoin(productInvestUser.id.product, product)
                .where(
                        productInvestUser.id.userId.eq(userId)
                )
                .orderBy(productInvestUser.lastModifiedDate.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetchResults();

    }

}

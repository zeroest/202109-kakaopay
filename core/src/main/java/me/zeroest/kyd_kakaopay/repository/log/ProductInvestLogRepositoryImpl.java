package me.zeroest.kyd_kakaopay.repository.log;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import me.zeroest.kyd_kakaopay.dto.product.QMyInvestDto;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.Objects;

import static me.zeroest.kyd_kakaopay.domain.invest.log.QProductInvestLog.productInvestLog;
import static me.zeroest.kyd_kakaopay.domain.product.QProduct.product;

public class ProductInvestLogRepositoryImpl implements ProductInvestLogRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductInvestLogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public QueryResults<MyInvestDto> findMyInvest(String userId, Pageable page) {

        return queryFactory.select(new QMyInvestDto(
                productInvestLog.id, product.id, product.title,
                        product.totalAmount, productInvestLog.investAmount, productInvestLog.createdDate,
                        productInvestLog.accrueUserInvest, productInvestLog.investResult, productInvestLog.userId
                ))
                .from(productInvestLog)
                .innerJoin(productInvestLog.product, product)
                .where(
                        productInvestLog.userId.eq(userId)
                        , productInvestLog.investResult.eq(InvestResult.SUCCESS)
                )
                .orderBy(productInvestLog.id.desc())
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetchResults();

    }

    @Override
    public long lastAccrueUserInvest(String userId, long productId) {

        Long lastAccrueUserInvest = queryFactory.select(productInvestLog.accrueUserInvest.nullif(0L))
                .from(productInvestLog)
                .where(
                        productInvestLog.userId.eq(userId)
                        , productInvestLog.investResult.eq(InvestResult.SUCCESS)
                        , productInvestLog.product.id.eq(productId)
                )
                .orderBy(productInvestLog.id.desc())
                .limit(1L)
                .fetchOne();

        if (Objects.isNull(lastAccrueUserInvest)) {
            lastAccrueUserInvest = 0L;
        }

        return lastAccrueUserInvest;

    }
}

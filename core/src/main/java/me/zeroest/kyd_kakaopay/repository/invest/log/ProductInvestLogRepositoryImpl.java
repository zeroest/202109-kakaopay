package me.zeroest.kyd_kakaopay.repository.invest.log;

import com.querydsl.jpa.impl.JPAQueryFactory;
import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;

import javax.persistence.EntityManager;

import java.util.Objects;

import static me.zeroest.kyd_kakaopay.domain.invest.log.QProductInvestLog.productInvestLog;

public class ProductInvestLogRepositoryImpl implements ProductInvestLogRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public ProductInvestLogRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
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

package me.zeroest.kyd_kakaopay.domain.invest.log;

import lombok.*;
import me.zeroest.kyd_kakaopay.domain.base.BaseTimeEntity;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "PRODUCT_INVEST_LOG")
@Entity
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInvestLog extends BaseTimeEntity {

    @Builder
    public ProductInvestLog(Long id, String userId, Long investAmount, Product product, InvestResult investResult, String failReason, Long accrueUserInvest, Long accrueProductInvest) {
        this.id = id;
        this.userId = userId;
        this.investAmount = investAmount;
        this.product = product;
        this.investResult = investResult;
        this.failReason = failReason;
        this.accrueUserInvest = accrueUserInvest;
        this.accrueProductInvest = accrueProductInvest;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long id;


    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "invest_amount", nullable = false)
    private Long investAmount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product product;


    @Enumerated(EnumType.STRING)
    @Column(name = "invest_result", nullable = false)
    private InvestResult investResult;

    @Column(name = "fail_reason")
    private String failReason;


    @Column(name = "accrue_user_invest", nullable = false)
    private Long accrueUserInvest;

    @Column(name = "accrue_product_invest", nullable = false)
    private Long accrueProductInvest;

    public void updateLogSuccess() {
        this.investResult = InvestResult.SUCCESS;
        this.failReason = null;
    }

    public void updateLogFail(String failReason) {
        this.failReason = failReason;
    }

}

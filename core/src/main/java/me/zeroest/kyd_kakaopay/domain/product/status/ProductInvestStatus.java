package me.zeroest.kyd_kakaopay.domain.product.status;

import lombok.*;
import me.zeroest.kyd_kakaopay.domain.base.BaseTimeEntity;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "PRODUCT_INVEST_STATUS")
@Entity
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInvestStatus extends BaseTimeEntity {

    public static final String REDIS_INVESTED_AMOUNT_PREFIX = "product:invested:amount:";
    public static final String REDIS_INVESTING_CNT_PREFIX = "product:investing:cnt:";
    public static final String REDIS_LOCK_PRODUCT_ID_PREFIX = "lock:product:id:";

    @Builder
    public ProductInvestStatus(Long id, Product product, Long investedAmount, Long investingCnt, InvestStatus investStatus) {
        this.id = id;
        this.product = product;
        this.investedAmount = investedAmount;
        this.investingCnt = investingCnt;
        this.investStatus = investStatus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "status_id", nullable = false)
    private Long id;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product product;

    @Column(name = "invested_amount", nullable = false)
    private Long investedAmount;

    @Column(name = "investing_cnt", nullable = false)
    private Long investingCnt;

    @Enumerated(EnumType.STRING)
    @Column(name = "investing_status", nullable = false)
    private InvestStatus investStatus;


    public boolean isCompleted() {
        return investStatus == InvestStatus.COMPLETE;
    }

    public String getRedisInvestedAmountKey() {
        return REDIS_INVESTED_AMOUNT_PREFIX + product.getId();
    }
    public String getRedisLockKeyByProductId() {
        return REDIS_LOCK_PRODUCT_ID_PREFIX + product.getId();
    }

}

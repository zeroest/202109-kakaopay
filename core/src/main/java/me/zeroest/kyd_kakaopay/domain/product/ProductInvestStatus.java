package me.zeroest.kyd_kakaopay.domain.product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.zeroest.kyd_kakaopay.domain.base.BaseTimeEntity;
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
    public static final String REDIS_INVESTING_CNT = "product:investing:cnt:";

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

}

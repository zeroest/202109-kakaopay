package me.zeroest.kyd_kakaopay.domain.invest.log;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

}

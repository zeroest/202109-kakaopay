package me.zeroest.kyd_kakaopay.domain.invest.user;

import lombok.*;
import me.zeroest.kyd_kakaopay.domain.base.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Table(name = "PRODUCT_INVEST_USER")
@Entity
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductInvestUser extends BaseTimeEntity {

    @Builder
    public ProductInvestUser(ProductInvestUserId id, Long investAmount) {
        this.id = id;
        this.totalInvestAmount = investAmount;
    }

    @EmbeddedId
    private ProductInvestUserId id;

    @Column(name = "total_invest_amount", nullable = false)
    private Long totalInvestAmount;

    public void updateTotalInvestAmount(long totalInvestAmount) {
        this.totalInvestAmount = totalInvestAmount;
    }

}

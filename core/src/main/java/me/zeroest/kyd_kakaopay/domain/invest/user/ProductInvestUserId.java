package me.zeroest.kyd_kakaopay.domain.invest.user;

import lombok.*;
import me.zeroest.kyd_kakaopay.domain.product.Product;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Getter
@Embeddable
public class ProductInvestUserId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private String userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Product product;

}

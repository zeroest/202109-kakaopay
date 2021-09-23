package me.zeroest.kyd_kakaopay.domain.product;

import lombok.*;
import me.zeroest.kyd_kakaopay.domain.base.BaseTimeEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "PRODUCT")
@Entity
@Getter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseTimeEntity implements Serializable {

    @Builder
    public Product(Long id, String title, Long totalAmount, LocalDateTime startedAt, LocalDateTime finishedAt) {
        this.id = id;
        this.title = title;
        this.totalAmount = totalAmount;
        this.startedAt = startedAt;
        this.finishedAt = finishedAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Long id;


    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "total_investing_amount", nullable = false)
    private Long totalAmount;


    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "finished_at", nullable = false)
    private LocalDateTime finishedAt;

}

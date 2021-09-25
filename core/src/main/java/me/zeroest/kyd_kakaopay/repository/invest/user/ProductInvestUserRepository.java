package me.zeroest.kyd_kakaopay.repository.invest.user;

import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUser;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUserId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInvestUserRepository extends JpaRepository<ProductInvestUser, ProductInvestUserId>, ProductInvestUserRepositoryCustom {
}

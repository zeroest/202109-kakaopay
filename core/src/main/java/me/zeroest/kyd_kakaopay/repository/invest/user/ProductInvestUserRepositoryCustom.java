package me.zeroest.kyd_kakaopay.repository.invest.user;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import org.springframework.data.domain.Pageable;

public interface ProductInvestUserRepositoryCustom {

    QueryResults<MyInvestDto> findMyInvest(String userId, Pageable page);

}

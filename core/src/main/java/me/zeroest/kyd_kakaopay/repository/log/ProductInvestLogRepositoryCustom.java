package me.zeroest.kyd_kakaopay.repository.log;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import org.springframework.data.domain.Pageable;

public interface ProductInvestLogRepositoryCustom {

    QueryResults<MyInvestDto> findMyInvest(String userId, Pageable page);

    long lastAccrueUserInvest(String userId, long productId);

}

package me.zeroest.kyd_kakaopay.service;

import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.repository.log.ProductInvestLogRepository;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRedisService productRedisService;

    private final ProductRepository productRepository;
    private final ProductInvestLogRepository productInvestLogRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductDto> getAllProduct(LocalDateTime now, Pageable page) {

        QueryResults<Product> productAll = productRepository.findProductAll(now, page);

        List<ProductDto> productDtoList = new ArrayList<>();

        for (Product product : productAll.getResults()) {
            productDtoList.add(new ProductDto(
                    product,
                    productRedisService.getInvestedAmount(product),
                    productRedisService.getInvestingCnt(product)
            ));
        }

        return new PageResponse(productDtoList, productAll.getTotal());

    }

    @CacheEvict(value = "findProductAll", key = "#page")
    public void getAllProductRefresh(Pageable page) {
    }

    @Transactional(readOnly = true)
    public PageResponse<MyInvestDto> getMyInvest(String userId, Pageable page) {

        final QueryResults<MyInvestDto> myInvest = productInvestLogRepository.findMyInvest(userId, page);

        return new PageResponse(myInvest.getResults(), myInvest.getTotal());
    }

}

package me.zeroest.kyd_kakaopay.service;

import com.querydsl.core.QueryResults;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductDto> getAllProduct(LocalDateTime now, Pageable page) {

        QueryResults<Product> productAll = productRepository.findProductAll(now, page);

        List<ProductDto> productDtoList = productAll.getResults()
                .stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        return new PageResponse(productDtoList, productAll.getTotal());

    }

}

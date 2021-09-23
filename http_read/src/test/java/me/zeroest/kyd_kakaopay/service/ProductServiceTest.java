package me.zeroest.kyd_kakaopay.service;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.product.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductRedisService productRedisService;

    @InjectMocks
    private ProductService productService;

    @DisplayName("모든 Product 리스트를 ProductDto 리스트로 반환 한다.")
    @Test
    void getAllProduct() {

        LocalDateTime startAt = LocalDateTime.of(2021, 9, 23, 13, 0);
        LocalDateTime finishedAt = LocalDateTime.of(2021, 9, 26, 13, 0);

        QueryResults<Product> allResult = new QueryResults(Arrays.asList(
                Product.builder()
                        .title("product1")
                        .totalAmount(100L)
                        .startedAt(startAt)
                        .finishedAt(finishedAt)
                        .build()
        ), 10L, 0L, 1);

        when(productRepository.findProductAll(any(), any()))
                .thenReturn(allResult);

        when(productRedisService.getInvestingCnt(any(Product.class)))
                .thenReturn(10L);
        when(productRedisService.getInvestedAmount(any(Product.class)))
                .thenReturn(10L);

        PageResponse<ProductDto> allProduct1 = productService.getAllProduct(LocalDateTime.now(), PageRequest.of(0, 10));

        ProductDto productDto1 = allProduct1.getList().get(0);
        assertEquals(1, allProduct1.getTotalCount());
        assertEquals("product1", productDto1.getTitle());
        assertEquals(100L, productDto1.getTotalInvestingAmount());
        assertEquals(startAt, productDto1.getStartedAt());
        assertEquals(finishedAt, productDto1.getFinishedAt());
        assertEquals(10L, productDto1.getInvestingCnt());
        assertEquals(10L, productDto1.getCurrentInvestingAmount());
        assertEquals(InvestStatus.ONGOING, productDto1.getInvestStatus());

    }

    @DisplayName("현재 투자금액이 총 투자 모집 금액보다 크거나 같을경우 InvestStatus 는 Complete를 반환한다.")
    @Test
    void getAllProductStatusComplete() {

        LocalDateTime startAt = LocalDateTime.of(2021, 9, 23, 13, 0);
        LocalDateTime finishedAt = LocalDateTime.of(2021, 9, 26, 13, 0);

        QueryResults<Product> allResult = new QueryResults(Arrays.asList(
                Product.builder()
                        .title("product1")
                        .totalAmount(100L)
                        .startedAt(startAt)
                        .finishedAt(finishedAt)
                        .build()
        ), 10L, 0L, 1);

        when(productRepository.findProductAll(any(), any()))
                .thenReturn(allResult);

        when(productRedisService.getInvestingCnt(any(Product.class)))
                .thenReturn(10L);
        when(productRedisService.getInvestedAmount(any(Product.class)))
                .thenReturn(100L);

        PageResponse<ProductDto> allProduct2 = productService.getAllProduct(LocalDateTime.now(), PageRequest.of(0, 10));

        ProductDto productDto2 = allProduct2.getList().get(0);
        assertEquals(InvestStatus.COMPLETE, productDto2.getInvestStatus());

    }

}
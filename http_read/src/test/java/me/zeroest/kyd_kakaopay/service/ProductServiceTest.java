package me.zeroest.kyd_kakaopay.service;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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

        Mockito.when(productRepository.findProductAll(Mockito.any(), Mockito.any()))
                .thenReturn(allResult);

        PageResponse<ProductDto> allProduct = productService.getAllProduct(LocalDateTime.now(), PageRequest.of(0, 10));

        ProductDto productDto = allProduct.getList().get(0);
        assertEquals(1, allProduct.getTotalCount());
        assertEquals("product1", productDto.getTitle());
        assertEquals(100L, productDto.getTotalInvestingAmount());
        assertEquals(startAt, productDto.getStartedAt());
        assertEquals(finishedAt, productDto.getFinishedAt());

    }
}
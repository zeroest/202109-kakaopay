package me.zeroest.kyd_kakaopay.controller;

import me.zeroest.kyd_kakaopay.domain.product.status.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private PageResponse<ProductDto> expected;

    @BeforeEach
    void beforeEach() {
        List<ProductDto> productDtoList = Arrays.asList(
                new ProductDto(
                        Product.builder()
                                .id(1L)
                                .title("product1")
                                .totalAmount(50L)
                                .startedAt(LocalDateTime.now())
                                .finishedAt(LocalDateTime.now())
                                .build()
                        , 10L
                        , 10L
                ),
                new ProductDto(
                        Product.builder()
                                .id(2L)
                                .title("product1")
                                .totalAmount(50L)
                                .startedAt(LocalDateTime.now())
                                .finishedAt(LocalDateTime.now())
                                .build()
                        , 50L
                        , 10L
                )
        );

        expected = new PageResponse(productDtoList, (long) productDtoList.size());
    }

    @DisplayName("GET /product 를 통해 Product 리스트를 받아온다.")
    @Test
    void allProduct() throws Exception {

        when(productService.getAllProduct(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(expected);

        int page = 0;
        int size = 10;

        mvc.perform(MockMvcRequestBuilders.get("/product?page="+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(expected.getTotalCount()))
                .andExpect(jsonPath("$.data.list[0].productId").value(1L))
                .andExpect(jsonPath("$.data.list[0].investStatus").value(InvestStatus.ONGOING.toString()))
                .andExpect(jsonPath("$.data.list[1].productId").value(2L))
                .andExpect(jsonPath("$.data.list[1].investStatus").value(InvestStatus.COMPLETE.toString()));

    }

}
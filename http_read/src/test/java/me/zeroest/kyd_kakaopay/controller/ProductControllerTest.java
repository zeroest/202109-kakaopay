package me.zeroest.kyd_kakaopay.controller;

import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;
import me.zeroest.kyd_kakaopay.domain.product.status.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProductService productService;

    private PageResponse<ProductDto> allProductExpected;
    private PageResponse<MyInvestDto> getMyInvestExpected;

    private String userId = "userId";

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
        allProductExpected = new PageResponse(productDtoList, (long) productDtoList.size());

        final List<MyInvestDto> myInvestDtoList = Arrays.asList(
                MyInvestDto.builder()
                        .logId(2L)
                        .productId(1L)
                        .title("product")
                        .totalInvestingAmount(1000L)
                        .myInvestingAmount(100L)
                        .investDate(LocalDateTime.now())
                        .accrueUserInvest(200L)
                        .investResult(InvestResult.SUCCESS)
                        .userId(userId)
                        .build(),
                MyInvestDto.builder()
                        .logId(1L)
                        .productId(1L)
                        .title("product")
                        .totalInvestingAmount(1000L)
                        .myInvestingAmount(100L)
                        .investDate(LocalDateTime.now())
                        .accrueUserInvest(100L)
                        .investResult(InvestResult.SUCCESS)
                        .userId(userId)
                        .build()
        );
        getMyInvestExpected = new PageResponse(myInvestDtoList, (long) myInvestDtoList.size());

    }

    @DisplayName("GET /product 를 통해 Product 리스트를 받아온다.")
    @Test
    void allProduct() throws Exception {

        when(productService.getAllProduct(any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(allProductExpected);

        int page = 0;
        int size = 10;

        mvc.perform(MockMvcRequestBuilders.get("/api/product?page="+page+"&size="+size))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(allProductExpected.getTotalCount()))
                .andExpect(jsonPath("$.data.list[0].productId").value(1L))
                .andExpect(jsonPath("$.data.list[0].investStatus").value(InvestStatus.ONGOING.toString()))
                .andExpect(jsonPath("$.data.list[1].productId").value(2L))
                .andExpect(jsonPath("$.data.list[1].investStatus").value(InvestStatus.COMPLETE.toString()));

    }

    @DisplayName("GET /product/my/invest 를 통해 MyInvestDto 리스트를 받아온다.")
    @Test
    void getMyInvest() throws Exception {

        when(productService.getMyInvest(anyString(), any(Pageable.class)))
                .thenReturn(getMyInvestExpected);

        int page = 0;
        int size = 10;

        mvc.perform(MockMvcRequestBuilders.get("/api/product/my/invest?page="+page+"&size="+size).header("X-USER-ID", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data.totalCount").value(getMyInvestExpected.getTotalCount()))
                .andExpect(jsonPath("$.data.list[0].logId").value(2L))
                .andExpect(jsonPath("$.data.list[0].productId").value(1L))
                .andExpect(jsonPath("$.data.list[0].totalInvestingAmount").value(1000L))
                .andExpect(jsonPath("$.data.list[0].myInvestingAmount").value(100L))
                .andExpect(jsonPath("$.data.list[0].accrueUserInvest").value(200L))
                .andExpect(jsonPath("$.data.list[0].investResult").value(InvestResult.SUCCESS.toString()))
                .andExpect(jsonPath("$.data.list[0].userId").value(userId))
                .andExpect(jsonPath("$.data.list[1].logId").value(1L))
                .andExpect(jsonPath("$.data.list[1].productId").value(1L))
                .andExpect(jsonPath("$.data.list[1].totalInvestingAmount").value(1000L))
                .andExpect(jsonPath("$.data.list[1].myInvestingAmount").value(100L))
                .andExpect(jsonPath("$.data.list[1].accrueUserInvest").value(100L))
                .andExpect(jsonPath("$.data.list[1].investResult").value(InvestResult.SUCCESS.toString()))
                .andExpect(jsonPath("$.data.list[1].userId").value(userId));

    }

    @DisplayName("GET /product/my/invest 에 X-USER-ID 가 없을시 MISSING_X_USER_ID 에러를 반환한다.")
    @Test
    void getMyInvestMissingUserId() throws Exception {

        final ExceptionCode expected = ExceptionCode.MISSING_X_USER_ID;

        int page = 0;
        int size = 10;

        mvc.perform(MockMvcRequestBuilders.get("/api/product/my/invest?page="+page+"&size="+size))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(expected.getCode()))
                .andExpect(jsonPath("$.message").value(expected.getMessage()));

    }

}
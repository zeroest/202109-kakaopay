package me.zeroest.kyd_kakaopay.repository.log;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.invest.log.InvestResult;
import me.zeroest.kyd_kakaopay.domain.invest.log.ProductInvestLog;
import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.dto.product.MyInvestDto;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductInvestLogRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInvestLogRepository productInvestLogRepository;

    private String userId = "userId";

    @BeforeEach
    void beforeEach() {

        Product product1 = Product.builder()
                .title("product1")
                .totalAmount(1000L)
                .startedAt(LocalDateTime.of(2021, 9, 22, 9, 0))
                .finishedAt(LocalDateTime.of(2021, 9, 23, 21, 0))
                .build();
        productRepository.save(product1);


        List<ProductInvestLog> logList = new ArrayList<>();

        ProductInvestLog log1 = ProductInvestLog.builder()
                .userId(userId)
                .investAmount(100L)
                .product(product1)
                .investResult(InvestResult.SUCCESS)
                .accrueUserInvest(100L)
                .accrueProductInvest(100L)
                .build();
        logList.add(log1);

        ProductInvestLog log2 = ProductInvestLog.builder()
                .userId(userId)
                .investAmount(200L)
                .product(product1)
                .investResult(InvestResult.SUCCESS)
                .accrueUserInvest(300L)
                .accrueProductInvest(300L)
                .build();
        logList.add(log2);

        ProductInvestLog log3 = ProductInvestLog.builder()
                .userId(userId)
                .investAmount(200L)
                .product(product1)
                .investResult(InvestResult.FAIL)
                .accrueUserInvest(300L)
                .accrueProductInvest(300L)
                .build();
        logList.add(log3);

        ProductInvestLog log4 = ProductInvestLog.builder()
                .userId(userId)
                .investAmount(20L)
                .product(product1)
                .investResult(InvestResult.SUCCESS)
                .accrueUserInvest(320L)
                .accrueProductInvest(320L)
                .build();
        logList.add(log4);

        ProductInvestLog log5 = ProductInvestLog.builder()
                .userId("userId2")
                .investAmount(20L)
                .product(product1)
                .investResult(InvestResult.SUCCESS)
                .accrueUserInvest(20L)
                .accrueProductInvest(340L)
                .build();
        logList.add(log5);

        productInvestLogRepository.saveAll(logList);

    }

    @DisplayName("findMyInvest 은 MyInvestDto 를 페이징하여 반환한다.")
    @Test
    void findMyInvestPaging() {

        QueryResults<MyInvestDto> myInvestPage = productInvestLogRepository.findMyInvest(userId, PageRequest.of(0, 1));

        List<MyInvestDto> myInvestDtoList = myInvestPage.getResults();
        assertEquals(3, myInvestPage.getTotal());
        assertEquals(1, myInvestDtoList.size());

    }

    @DisplayName("findMyInvest 은 investResult 가 SUCCESS 인 데이터만 반환한다.")
    @Test
    void findMyInvestSuccess() {

        QueryResults<MyInvestDto> myInvestPage = productInvestLogRepository.findMyInvest(userId, PageRequest.of(0, 10));

        List<MyInvestDto> myInvestDtoList = myInvestPage.getResults();

        for (MyInvestDto dto : myInvestDtoList) {
            assertEquals(InvestResult.SUCCESS, dto.getInvestResult());
        }

    }

    @DisplayName("findMyInvest 은 userId 를 기준으로 자신의 기록만 반환한다.")
    @Test
    void findMyInvestOwn() {

        QueryResults<MyInvestDto> myInvestPage = productInvestLogRepository.findMyInvest(userId, PageRequest.of(0, 10));

        List<MyInvestDto> myInvestDtoList = myInvestPage.getResults();

        for (MyInvestDto dto : myInvestDtoList) {
            assertEquals(userId, dto.getUserId());
        }

    }

}
package me.zeroest.kyd_kakaopay.repository.invest.user;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUser;
import me.zeroest.kyd_kakaopay.domain.invest.user.ProductInvestUserId;
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
class ProductInvestUserRepositoryImplTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInvestUserRepository productInvestUserRepository;

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

        Product product2 = Product.builder()
                .title("product2")
                .totalAmount(2000L)
                .startedAt(LocalDateTime.of(2021, 9, 22, 9, 0))
                .finishedAt(LocalDateTime.of(2021, 9, 23, 21, 0))
                .build();
        productRepository.save(product2);


        List<ProductInvestUser> investUserList = new ArrayList<>();

        ProductInvestUser investUser1 = ProductInvestUser.builder()
                .id(
                        new ProductInvestUserId(userId, product1)
                )
                .investAmount(100L)
                .build();
        investUserList.add(investUser1);

        ProductInvestUser investUser2 = ProductInvestUser.builder()
                .id(
                        new ProductInvestUserId(userId, product2)
                )
                .investAmount(200L)
                .build();
        investUserList.add(investUser2);

        ProductInvestUser investUser3 = ProductInvestUser.builder()
                .id(
                        new ProductInvestUserId("userId2", product2)
                )
                .investAmount(200L)
                .build();
        investUserList.add(investUser3);

        productInvestUserRepository.saveAll(investUserList);

    }


    @DisplayName("findMyInvest 은 MyInvestDto 를 페이징하여 반환한다.")
    @Test
    void findMyInvestPaging() {

        QueryResults<MyInvestDto> myInvestPage = productInvestUserRepository.findMyInvest(userId, PageRequest.of(0, 1));

        List<MyInvestDto> myInvestDtoList = myInvestPage.getResults();
        assertEquals(2, myInvestPage.getTotal());
        assertEquals(1, myInvestDtoList.size());

    }

    @DisplayName("findMyInvest 은 userId 를 기준으로 자신의 기록만 반환한다.")
    @Test
    void findMyInvestOwn() {

        QueryResults<MyInvestDto> myInvestPage = productInvestUserRepository.findMyInvest(userId, PageRequest.of(0, 10));

        List<MyInvestDto> myInvestDtoList = myInvestPage.getResults();

        for (MyInvestDto dto : myInvestDtoList) {
            assertEquals(userId, dto.getUserId());
        }

    }

}
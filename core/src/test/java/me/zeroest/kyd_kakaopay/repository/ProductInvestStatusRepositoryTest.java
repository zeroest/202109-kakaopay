package me.zeroest.kyd_kakaopay.repository;

import me.zeroest.kyd_kakaopay.domain.product.Product;
import me.zeroest.kyd_kakaopay.domain.product.status.InvestStatus;
import me.zeroest.kyd_kakaopay.domain.product.status.ProductInvestStatus;
import me.zeroest.kyd_kakaopay.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductInvestStatusRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductInvestStatusRepository productInvestStatusRepository;

    @BeforeEach
    void beforeEach() {

        Product product1 = Product.builder()
                .title("product1")
                .totalAmount(100L)
                .startedAt(LocalDateTime.of(2021, 9, 21, 9, 0))
                .finishedAt(LocalDateTime.of(2021, 9, 22, 21, 0))
                .build();
        productRepository.save(product1);

        ProductInvestStatus productInvestStatus = ProductInvestStatus.builder()
                .product(product1)
                .investedAmount(30L)
                .investingCnt(3L)
                .investStatus(InvestStatus.ONGOING)
                .build();
        productInvestStatusRepository.save(productInvestStatus);

    }

    @Test
    void findByProductId() {

        Optional<ProductInvestStatus> optionalInvestStatus = productInvestStatusRepository.findByProductId(1L);

        assertTrue(optionalInvestStatus.isPresent());

        ProductInvestStatus investStatus = optionalInvestStatus.get();

        assertEquals(30L, investStatus.getInvestedAmount());
        assertEquals(3L, investStatus.getInvestingCnt());
        assertEquals(InvestStatus.ONGOING, investStatus.getInvestStatus());

        Product product = investStatus.getProduct();
        assertNotNull(product);
        assertEquals("product1", product.getTitle());
        assertEquals(100L, product.getTotalAmount());

    }

}
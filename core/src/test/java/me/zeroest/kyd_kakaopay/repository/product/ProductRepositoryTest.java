package me.zeroest.kyd_kakaopay.repository.product;

import com.querydsl.core.QueryResults;
import me.zeroest.kyd_kakaopay.domain.product.Product;
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
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void beforeEach() {

        List<Product> products = new ArrayList<>();

        Product product1 = Product.builder()
                .title("product1")
                .totalAmount(100L)
                .startedAt(LocalDateTime.of(2021, 9, 21, 9, 0))
                .finishedAt(LocalDateTime.of(2021, 9, 22, 21, 0))
                .build();
        products.add(product1);

        Product product2 = Product.builder()
                .title("product2")
                .totalAmount(100L)
                .startedAt(LocalDateTime.of(2021, 9, 22, 9, 0))
                .finishedAt(LocalDateTime.of(2021, 9, 23, 21, 0))
                .build();
        products.add(product2);

        Product product3 = Product.builder()
                .title("product3")
                .totalAmount(100L)
                .startedAt(LocalDateTime.of(2021, 9, 21, 14, 1))
                .finishedAt(LocalDateTime.of(2021, 9, 23, 21, 0))
                .build();
        products.add(product3);

        productRepository.saveAll(products);

    }

    @DisplayName("findProductAll 은 Product 를 페이징하여 반환한다.")
    @Test
    void getAllProductPaging() {

        final LocalDateTime now2 = LocalDateTime.of(2021, 9, 21, 14, 2);
        final QueryResults<Product> productAll2 = productRepository.findProductAll(
                now2,
                PageRequest.of(0, 1)
        );
        final List<Product> allProduct2 = productAll2.getResults();
        assertEquals(2, productAll2.getTotal());
        assertEquals(1, allProduct2.size());
        assertEquals("product3", allProduct2.get(0).getTitle());

    }

    @DisplayName("findProductAll 은 startedAt finishedAt 사이에 존재하는 Product 를 반환한다.")
    @Test
    void getAllProduct() {

        final LocalDateTime now1 = LocalDateTime.of(2021, 9, 21, 14, 0);

        final QueryResults<Product> productAll1 = productRepository.findProductAll(
                now1,
                PageRequest.of(0, 10)
        );
        final List<Product> allProduct1 = productAll1.getResults();
        assertEquals(1, productAll1.getTotal());
        assertEquals("product1", allProduct1.get(0).getTitle());
        assertTrue(allProduct1.get(0).getStartedAt().isBefore(now1));
        assertTrue(allProduct1.get(0).getFinishedAt().isAfter(now1));

    }

}
package me.zeroest.kyd_kakaopay.controller;

import lombok.RequiredArgsConstructor;
import me.zeroest.kyd_kakaopay.dto.product.ProductDto;
import me.zeroest.kyd_kakaopay.dto.response.PageResponse;
import me.zeroest.kyd_kakaopay.service.ProductService;
import me.zeroest.kyd_kakaopay.util.ApiUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<PageResponse<ProductDto>> allProduct(
            Pageable page
    ) {

        final PageResponse<ProductDto> allProductPage = productService.getAllProduct(LocalDateTime.now(), page);

        return ApiUtil.success(allProductPage);

    }

}

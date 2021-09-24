package me.zeroest.kyd_kakaopay.controller;

import lombok.RequiredArgsConstructor;
import me.zeroest.kyd_kakaopay.dto.InvestProductRequest;
import me.zeroest.kyd_kakaopay.dto.request.CommonRequest;
import me.zeroest.kyd_kakaopay.dto.response.CommonResponse;
import me.zeroest.kyd_kakaopay.util.ApiUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/invest")
public class InvestController {

    @PostMapping
    public ResponseEntity<String> investProduct(
            @RequestHeader(CommonRequest.X_USER_ID) String userId,
            @Valid @RequestBody InvestProductRequest request
    ) {

        return ApiUtil.success(CommonResponse.OK);

    }

}

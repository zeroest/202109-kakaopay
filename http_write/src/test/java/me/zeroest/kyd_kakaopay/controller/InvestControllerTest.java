package me.zeroest.kyd_kakaopay.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.zeroest.kyd_kakaopay.dto.InvestProductRequest;
import me.zeroest.kyd_kakaopay.dto.request.CommonRequest;
import me.zeroest.kyd_kakaopay.dto.response.CommonResponse;
import me.zeroest.kyd_kakaopay.exception.ExceptionCode;
import me.zeroest.kyd_kakaopay.service.InvestService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InvestController.class)
class InvestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private InvestService investService;


    @DisplayName("POST /api/invest 상품 투자를 한다.")
    @Test
    void investProduct() throws Exception {

        InvestProductRequest request = InvestProductRequest.builder()
                .productId(1L)
                .investAmount(1L)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.data").value(CommonResponse.OK));

    }

    @DisplayName("POST /api/invest 에 X-USER-ID 가 없을시 MISSING_X_USER_ID 에러를 반환한다.")
    @Test
    void investProductMissingUserId() throws Exception {

        final ExceptionCode expected = ExceptionCode.MISSING_X_USER_ID;

        mvc.perform(MockMvcRequestBuilders.post("/api/invest"))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value(expected.getCode()))
                .andExpect(jsonPath("$.message").value(expected.getMessage()));

    }

    @DisplayName("POST /api/invest 에 productId 는 null 또는 음수일 수 없다.")
    @Test
    void investProductProductId() throws Exception {

        InvestProductRequest requestNull = InvestProductRequest.builder()
                .productId(null)
                .investAmount(1L)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestNull))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value("00"));


        InvestProductRequest requestMinus = InvestProductRequest.builder()
                .productId(-1L)
                .investAmount(1L)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestMinus))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value("00"));

    }

    @DisplayName("POST /api/invest 에 investAmount 는 null 일 수 없고 0 이상이어야 한다.")
    @Test
    void investProductInvestAmount() throws Exception {

        InvestProductRequest requestNull = InvestProductRequest.builder()
                .productId(1L)
                .investAmount(null)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestNull))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value("00"));


        InvestProductRequest requestMinus = InvestProductRequest.builder()
                .productId(1L)
                .investAmount(-1L)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestMinus))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value("00"));

        InvestProductRequest requestZero = InvestProductRequest.builder()
                .productId(1L)
                .investAmount(0L)
                .build();

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/invest")
                                .header(CommonRequest.X_USER_ID, "userId")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestZero))
                )
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.code").value("00"));

    }

}

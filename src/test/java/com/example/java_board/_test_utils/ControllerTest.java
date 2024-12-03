package com.example.java_board._test_utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@AutoConfigureRestDocs
@ExtendWith({RestDocumentationExtension.class})
public class ControllerTest {
    /*
        컨트롤러 테스트를 위한 기본 공통 설정 제공
        1. 인코딩 필터, 출력 핸들러 등
        2. MockMvc를 설정하여 실제 서버를 띄우지 않고도 컨트롤러 동작 테스트 환경 제공
        3. Docs 자동생성 관련 설정
    */
    public ObjectMapper objectMapper = new ObjectMapper();
    public MockMvc mockMvc; // 실제 서버를 띄우지 않고도 컨트롤러 테스트 가능

    @BeforeEach
    public void setUp(
            final WebApplicationContext webApplicationContext,
            final RestDocumentationContextProvider restDocumentationContextProvider
    ) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(MockMvcResultHandlers.print())
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentationContextProvider)
                        .operationPreprocessors()
                        .withRequestDefaults(Preprocessors.prettyPrint())
                        .withResponseDefaults(Preprocessors.prettyPrint()))
                .build();
    }

}


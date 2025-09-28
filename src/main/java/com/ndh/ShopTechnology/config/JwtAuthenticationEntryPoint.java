package com.ndh.ShopTechnology.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndh.ShopTechnology.def.DefRes;
import com.ndh.ShopTechnology.dto.response.APIResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Serializable;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private final ObjectMapper objectMapper;

    public JwtAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        APIResponse apiResponse = APIResponse.doResponse(
                DefRes.STAT_CODE, DefRes.STATUS_NOT_FOUND,
                DefRes.RES_DES, "Login failed. Invalid username or password."
        );

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}

package com.example.imenu_spring_project.jwt;

import org.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.ServerException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServerException {
        // 유효한 자격증명을 제공하지 않고 접근하려 할때 401
        String exception = (String) request.getAttribute("exception");

        JwtErrorCode errorCode;


        /**
         * 토큰이 없는 경우 예외처리
         */
        if(exception == null) {
            errorCode = JwtErrorCode.UNAUTHORIZEDException;
            setResponse(response, errorCode);
            return;
        }

        /**
         * 토큰이 만료된 경우 예외처리
         */
        if(exception.equals("ExpiredJwtException")) {
            errorCode = JwtErrorCode.ExpiredJwtException;
            setResponse(response, errorCode);
            return;
        }
        else if (exception.equals("MalformedJwtException")){
            errorCode= JwtErrorCode.InvalidToken;
            setResponse(response,errorCode);
            return;
        }
        else if(exception.equals("HijackException")){
            errorCode =JwtErrorCode.HijackJwtToken;
            setResponse(response,errorCode);
            return;
        }
    }

    private void setResponse(HttpServletResponse response, JwtErrorCode errorCode) throws IOException {
        JSONObject json = new JSONObject();
        response.setContentType("application/json;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);


        json.put("isSuccess",false);
        json.put("code", errorCode.getCode());
        json.put("message", errorCode.getMessage());
        response.getWriter().print(json);
    }
}
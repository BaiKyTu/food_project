package com.cybersoft.food_project.jwt;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Track;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    private Gson gson = new Gson();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //cắt header và lấy Token
        String token = getTokenFromHeader(request);
        if (token != null) {
            //kiểm tra xem token có phải do hệ thống của mình sinh ra ko
            if (jwtTokenHelper.validateToken(token)) {
                //token hợp lệ
                String json = jwtTokenHelper.decodeToken(token);
                Map<String, Object> map = gson.fromJson(json, Map.class);
                System.out.println("kiem tra " + json + " - " + map.get("type").toString());
                if (StringUtils.hasText(map.get("type").toString())
                        && !map.get("type").toString().equals("refresh")) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken("", "", new ArrayList<>());
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(authenticationToken);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        //Lấy giá trị Token ở Header có key là Authorization
        String strToken = request.getHeader("Authorization");
        if (StringUtils.hasText(strToken) && strToken.startsWith("Bearer ")) {
            //xử lý khi token hợp lệ
            //substring(): hàm dùng để cắt chuỗi
            String finalToken = strToken.substring(7);
            return finalToken;
        } else {
            return null;
        }
    }
}

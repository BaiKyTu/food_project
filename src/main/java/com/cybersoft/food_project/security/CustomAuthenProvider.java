package com.cybersoft.food_project.security;

import com.cybersoft.food_project.entity.UserEntity;
import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomAuthenProvider implements AuthenticationProvider {

    @Autowired
    LoginService loginService;

    //    @Autowired
//    PasswordEncoder passwordEncoder;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserEntity userEntity = loginService.checkLogin(userName);

        if (userEntity != null) {
            boolean isMatchPassword = passwordEncoder.matches(password, userEntity.getPassword());
            if (isMatchPassword)
                return new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword(), new ArrayList<>());
            else {
                return null;
            }
        } else {
            return null;
        }
//        System.out.println("Username: "+userName+" password: "+password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}

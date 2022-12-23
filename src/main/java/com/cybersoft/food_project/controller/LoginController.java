package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.jwt.JwtTokenHelper;
import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.cybersoft.food_project.payload.response.DataTokenResponse;
import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin  //cho phép những domain khác với domain của api truy cập vào
@RequestMapping("/signin")
public class LoginController {

    @Autowired
    LoginService loginService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenHelper jwtTokenHelper;

    @GetMapping("/test")
    public String test(){
        return "Hello";
    }

    private long expiredDate = 8 * 60 * 60 * 1000;
    private long refreshExpiredDate = 80 * 60 * 60 * 1000;



    @PostMapping("")
    public ResponseEntity<?>signin(@RequestBody SignInRequest signInRequest){

        //chứng thực:
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(signInRequest.getUsername(),signInRequest.getPassword());
        Authentication auth = authenticationManager.authenticate(authRequest);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        //tạo token:
        String token = jwtTokenHelper.generateToken(signInRequest.getUsername(),"authen",expiredDate);

        //refresh token:
        String refreshToken = jwtTokenHelper.generateToken(signInRequest.getUsername(),"refresh",refreshExpiredDate);

        //giải mã token:
//        String decodeToken = jwtTokenHelper.decodeToken(token);

        DataTokenResponse dataTokenResponse = new DataTokenResponse();
        dataTokenResponse.setToken(token);
        dataTokenResponse.setRefreshToken(refreshToken);
        
        //xuất data:
        DataResponse dataResponse = new DataResponse();
//        boolean isSuccess = loginService.checkLogin(signInRequest.getUsername(),signInRequest.getPassword());
        dataResponse.setStatus(HttpStatus.OK.value());
//        dataResponse.setSuccess(loginService.checkLogin(signInRequest.getUsername(),signInRequest.getPassword()));
        dataResponse.setSuccess(true);
        dataResponse.setDescription("");
        dataResponse.setData(dataTokenResponse);
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }



}

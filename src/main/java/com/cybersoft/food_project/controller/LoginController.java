package com.cybersoft.food_project.controller;

import com.cybersoft.food_project.payload.request.SignInRequest;
import com.cybersoft.food_project.payload.response.DataResponse;
import com.cybersoft.food_project.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/signin")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("")
    public ResponseEntity<?>signin(@RequestBody SignInRequest signInRequest){
        DataResponse dataResponse = new DataResponse();
//        boolean isSuccess = loginService.checkLogin(signInRequest.getUsername(),signInRequest.getPassword());
        dataResponse.setStatus(HttpStatus.OK.value());
        dataResponse.setSuccess(loginService.checkLogin(signInRequest.getUsername(),signInRequest.getPassword()));
        dataResponse.setDescription("");
        dataResponse.setData("");
        return new ResponseEntity<>(dataResponse, HttpStatus.OK);
    }



}

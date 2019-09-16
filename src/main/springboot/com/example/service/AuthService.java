package com.example.service;

import javax.servlet.http.HttpServletRequest;

import com.example.model.LoginModel;
import com.example.model.SignUpModel;
import com.example.response.base.BaseResponse;
import com.example.response.base.JwtAuthenticationResponse;

public interface AuthService {
	BaseResponse signUp(SignUpModel signUpRequest);
	
	AuthenticationResponse logIn(LoginModel loginRequest, HttpServletRequest req);
}

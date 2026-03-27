package com.zinn.zinnbe.services;

import jakarta.servlet.http.HttpServletResponse;

import com.zinn.zinnbe.DTOs.LoginRequest;
import com.zinn.zinnbe.DTOs.SignUpRequest;
import com.zinn.zinnbe.DTOs.SuccessMessage;

public interface AuthService {
    SuccessMessage login(LoginRequest request, HttpServletResponse response);
    SuccessMessage signUp(SignUpRequest request, HttpServletResponse response);
}

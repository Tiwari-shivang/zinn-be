package com.zinn.zinnbe.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zinn.zinnbe.models.Users;
import com.zinn.zinnbe.services.impl.SecurityService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private JWTUtils utils;
    @Autowired
    private SecurityService securityService;

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/auth")){
            filter.doFilter(request, response);
            return;
        }
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("access_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        if(token == null){
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    status: 401,
                    error: Missing token
                }
            """);
            return;
        }
        try{
        UserDetails securityModel = securityService.loadUserByUsername(utils.extractEmail(token));
        Users user = (Users) securityModel;
        if(SecurityContextHolder.getContext().getAuthentication() == null && utils.validateToken(token, user)){
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, securityModel.getAuthorities()));
            filter.doFilter(request, response);
        }
        }
        catch(Exception ex){
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                    status: 401,
                    error: Invalid token
                }
            """);
        }
    }
}

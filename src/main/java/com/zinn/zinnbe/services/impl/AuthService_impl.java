package com.zinn.zinnbe.services.impl;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.zinn.zinnbe.DTOs.LoginRequest;
import com.zinn.zinnbe.DTOs.SignUpRequest;
import com.zinn.zinnbe.DTOs.SuccessMessage;
import com.zinn.zinnbe.models.Role;
import com.zinn.zinnbe.models.Tenant;
import com.zinn.zinnbe.models.Users;
import com.zinn.zinnbe.repositories.RoleRepository;
import com.zinn.zinnbe.repositories.TenantRepository;
import com.zinn.zinnbe.repositories.UserRepository;
import com.zinn.zinnbe.security.JWTUtils;
import com.zinn.zinnbe.services.AuthService;

import jakarta.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService_impl implements AuthService {
    private static final String ACCESS_TOKEN_COOKIE = "access_token";
    private static final long ACCESS_TOKEN_MAX_AGE_SECONDS = Duration.ofHours(1).toSeconds();

    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final TenantRepository tenantRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;
    private final JWTUtils jwtUtils;

    public AuthService_impl(
            AuthenticationManager authManager,
            UserRepository userRepo,
            TenantRepository tenantRepo,
            RoleRepository roleRepo,
            PasswordEncoder encoder,
            JWTUtils jwtUtils) {
        this.authManager = authManager;
        this.userRepo = userRepo;
        this.tenantRepo = tenantRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public SuccessMessage login(LoginRequest request, HttpServletResponse response) {
        String normalizedEmail = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Email is required");
        }

        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password");
        }

        Users user = (Users) authentication.getPrincipal();
        setAccessTokenCookie(response, jwtUtils.buildToken(user));
        return new SuccessMessage("Logged in successfully");
    }

    @Override
    public SuccessMessage signUp(SignUpRequest request, HttpServletResponse response) {
        String normalizedEmail = request.getEmail() == null ? null : request.getEmail().trim().toLowerCase(Locale.ROOT);
        if (normalizedEmail == null || normalizedEmail.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Email is required");
        }
        // if (userRepo.findByEmail(normalizedEmail) != null) {
        //     throw new ResponseStatusException(CONFLICT, "Email already exists");
        // }

        Timestamp now = new Timestamp(System.currentTimeMillis());

        Tenant tenant = new Tenant();
        tenant.setName(request.getTenant_name());
        tenant.setIndustry(request.getIndustry());
        tenant.setWebUrl(request.getWeb_url());
        tenant.setDescription(request.getDescription());
        tenant.setCreatedAt(now);
        Tenant savedTenant = tenantRepo.saveAndFlush(tenant);

        Role role = roleRepo.findById(UUID.fromString(request.getRole_id()))
                .orElseThrow(() -> new ResponseStatusException(BAD_REQUEST, "Invalid role_id"));

        Users user = new Users();
        user.setName(request.getFullname());
        user.setEmail(normalizedEmail);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(role);
        user.setTenant(savedTenant);
        user.setCreatedAt(now);
        Users savedUser = userRepo.save(user);

        setAccessTokenCookie(response, jwtUtils.buildToken(savedUser));
        return new SuccessMessage("Created user and tenant");
    }

    private void setAccessTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(ACCESS_TOKEN_MAX_AGE_SECONDS)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}

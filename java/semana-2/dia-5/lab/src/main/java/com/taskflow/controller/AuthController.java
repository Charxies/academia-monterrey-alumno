package com.taskflow.controller;

import com.taskflow.dto.auth.AuthResponse;
import com.taskflow.dto.auth.LoginRequest;
import com.taskflow.dto.auth.RegisterRequest;
import com.taskflow.dto.auth.UserResponse;
import com.taskflow.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController — la puerta HTTP de la autenticación (MP-5). Bajo /auth (público en el filter chain).
 *   - POST /auth/register -> 201 con UserResponse (sin passwordHash).
 *   - POST /auth/login    -> 200 con AuthResponse(token).
 * Delega TODO en AuthService; la validación de forma la dispara @Valid (400 vía el advice de D3).
 *
 * STRETCH sugerido: GET /auth/me -> el UserResponse del usuario del token (lee el Authentication).
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Registro y login (devuelve JWT).")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** POST /auth/register — 201 con UserResponse. Username duplicado -> 409 (advice). */
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request) {
        // TODO MP-5: delegar en authService.register(request) y responder 201
        //   (ResponseEntity.status(HttpStatus.CREATED).body(...)).
        throw new UnsupportedOperationException("TODO MP-5: POST /auth/register");
    }

    /** POST /auth/login — 200 con AuthResponse(token). Credenciales malas -> 401 (advice). */
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        // TODO MP-5: return authService.login(request);
        throw new UnsupportedOperationException("TODO MP-5: POST /auth/login");
    }
}

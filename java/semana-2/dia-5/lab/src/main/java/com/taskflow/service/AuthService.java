package com.taskflow.service;

import com.taskflow.dto.auth.AuthResponse;
import com.taskflow.dto.auth.LoginRequest;
import com.taskflow.dto.auth.RegisterRequest;
import com.taskflow.dto.auth.UserResponse;
import com.taskflow.repository.UserRepository;
import com.taskflow.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthService — el negocio de /auth (MP-5 register/login, MP-7 token real). Tienes inyectado todo lo
 * que necesitas: userRepository, passwordEncoder, authenticationManager y jwtService.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * MP-5 — Alta de usuario. 201 con UserResponse (el hash nunca sale). Username duplicado -> 409.
     */
    public UserResponse register(RegisterRequest request) {
        // TODO MP-5:
        //   1) si userRepository.existsByUsername(request.username()) ->
        //          throw new UsernameAlreadyExistsException(request.username());   // el advice lo hace 409
        //   2) String hash = passwordEncoder.encode(request.password());           // BCrypt, nunca en claro
        //   3) guardar new User(null, username, hash, email, Role.USER)             // MP-3 añade passwordHash al User
        //   4) devolver un UserResponse(id, username, email, role)                  // SIN passwordHash
        throw new UnsupportedOperationException("TODO MP-5: implementar register");
    }

    /**
     * MP-5/MP-7 — Login. Autentica con el AuthenticationManager (BadCredentialsException -> 401) y, si
     * pasa, emite el JWT real. En MP-5 devuelve el stub AuthResponse("pendiente-jwt"); en MP-7 conecta
     * el token real (jwtService.generateToken) y muere el stub.
     */
    public AuthResponse login(LoginRequest request) {
        // TODO MP-5:
        //   Authentication auth = authenticationManager.authenticate(
        //       new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        //   (por ahora, cliffhanger) return new AuthResponse("pendiente-jwt");
        // TODO MP-7:
        //   UserDetails userDetails = (UserDetails) auth.getPrincipal();
        //   return new AuthResponse(jwtService.generateToken(userDetails));
        throw new UnsupportedOperationException("TODO MP-5/MP-7: implementar login");
    }
}

package com.helpdesk.service;

import com.helpdesk.dto.request.LoginRequest;
import com.helpdesk.dto.request.RegisterRequest;
import com.helpdesk.dto.response.AuthResponse;
import com.helpdesk.dto.response.UserResponse;
import com.helpdesk.entity.User;
import com.helpdesk.exception.BusinessException;
import com.helpdesk.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para autenticação
 */
@Service
@Transactional
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário e retorna token
     * @param request Dados do registro
     * @return Resposta de autenticação com token
     */
    public AuthResponse register(RegisterRequest request) {
        UserResponse userResponse = userService.register(request);
        String token = jwtTokenProvider.generateToken(userResponse.getId(), userResponse.getEmail());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setUsuario(userResponse);
        return response;
    }

    /**
     * Autentica um usuário e retorna token
     * @param request Dados do login
     * @return Resposta de autenticação com token
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail());

        if (!passwordEncoder.matches(request.getSenha(), user.getSenha())) {
            throw new BusinessException("Email ou senha inválidos");
        }

        String token = jwtTokenProvider.generateToken(user.getId(), user.getEmail());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setNome(user.getNome());
        userResponse.setPerfil(user.getPerfil());
        userResponse.setCreatedAt(user.getCreatedAt());

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setTipo("Bearer");
        response.setUsuario(userResponse);
        return response;
    }
}

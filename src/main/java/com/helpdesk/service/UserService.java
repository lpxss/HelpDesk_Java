package com.helpdesk.service;

import com.helpdesk.dto.request.RegisterRequest;
import com.helpdesk.dto.response.UserResponse;
import com.helpdesk.entity.User;
import com.helpdesk.entity.enums.Perfil;
import com.helpdesk.exception.BusinessException;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para operações com usuários
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Registra um novo usuário
     * @param request Dados do registro
     * @return Usuário criado
     */
    public UserResponse register(RegisterRequest request) {
        // Verifica se o email já existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado");
        }

        // Cria novo usuário com perfil USUARIO por padrão
        User user = new User();
        user.setEmail(request.getEmail());
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setNome(request.getNome());
        user.setPerfil(Perfil.USUARIO);

        User savedUser = userRepository.save(user);
        return toUserResponse(savedUser);
    }

    /**
     * Busca um usuário por ID
     * @param id ID do usuário
     * @return Usuário encontrado
     */
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return toUserResponse(user);
    }

    /**
     * Busca um usuário por email
     * @param email Email do usuário
     * @return Usuário encontrado
     */
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }

    /**
     * Converte entidade User para DTO UserResponse
     * @param user Entidade User
     * @return DTO UserResponse
     */
    private UserResponse toUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setNome(user.getNome());
        response.setPerfil(user.getPerfil());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}

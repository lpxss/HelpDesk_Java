package com.helpdesk.dto.response;

import com.helpdesk.entity.enums.Perfil;

import java.time.LocalDateTime;

/**
 * DTO para resposta de usuário
 */
public class UserResponse {

    private Long id;
    private String email;
    private String nome;
    private Perfil perfil;
    private LocalDateTime createdAt;

    // Construtores
    public UserResponse() {
    }

    public UserResponse(Long id, String email, String nome, Perfil perfil, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.nome = nome;
        this.perfil = perfil;
        this.createdAt = createdAt;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

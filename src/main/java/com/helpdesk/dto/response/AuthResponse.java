package com.helpdesk.dto.response;

/**
 * DTO para resposta de autenticação
 */
public class AuthResponse {

    private String token;
    private String tipo;
    private UserResponse usuario;

    // Construtores
    public AuthResponse() {
    }

    public AuthResponse(String token, String tipo, UserResponse usuario) {
        this.token = token;
        this.tipo = tipo;
        this.usuario = usuario;
    }

    // Getters e Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UserResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UserResponse usuario) {
        this.usuario = usuario;
    }
}

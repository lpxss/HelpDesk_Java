package com.helpdesk.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisição de criação de comentário
 */
public class CommentRequest {

    @NotBlank(message = "Texto do comentário é obrigatório")
    private String texto;

    // Construtores
    public CommentRequest() {
    }

    public CommentRequest(String texto) {
        this.texto = texto;
    }

    // Getters e Setters
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}

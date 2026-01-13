package com.helpdesk.dto.response;

import com.helpdesk.entity.enums.Categoria;
import com.helpdesk.entity.enums.Prioridade;
import com.helpdesk.entity.enums.Status;

import java.time.LocalDateTime;

/**
 * DTO para resposta de ticket
 */
public class TicketResponse {

    private Long id;
    private String titulo;
    private String descricao;
    private Categoria categoria;
    private Prioridade prioridade;
    private Status status;
    private UserResponse usuario;
    private UserResponse atendente;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtores
    public TicketResponse() {
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserResponse getUsuario() {
        return usuario;
    }

    public void setUsuario(UserResponse usuario) {
        this.usuario = usuario;
    }

    public UserResponse getAtendente() {
        return atendente;
    }

    public void setAtendente(UserResponse atendente) {
        this.atendente = atendente;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

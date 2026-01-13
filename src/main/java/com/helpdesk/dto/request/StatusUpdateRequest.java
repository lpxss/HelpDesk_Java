package com.helpdesk.dto.request;

import com.helpdesk.entity.enums.Status;
import jakarta.validation.constraints.NotNull;

/**
 * DTO para requisição de atualização de status do ticket
 */
public class StatusUpdateRequest {

    @NotNull(message = "Status é obrigatório")
    private Status status;

    // Construtores
    public StatusUpdateRequest() {
    }

    public StatusUpdateRequest(Status status) {
        this.status = status;
    }

    // Getters e Setters
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

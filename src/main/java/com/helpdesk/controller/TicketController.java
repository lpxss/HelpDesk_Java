package com.helpdesk.controller;

import com.helpdesk.dto.request.StatusUpdateRequest;
import com.helpdesk.dto.request.TicketRequest;
import com.helpdesk.dto.response.TicketResponse;
import com.helpdesk.security.SecurityUtil;
import com.helpdesk.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações com tickets
 */
@RestController
@RequestMapping("/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    /**
     * Endpoint para criar um novo ticket
     * POST /tickets
     */
    @PostMapping
    public ResponseEntity<TicketResponse> create(@Valid @RequestBody TicketRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        TicketResponse response = ticketService.create(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para listar todos os tickets (conforme perfil)
     * GET /tickets
     */
    @GetMapping
    public ResponseEntity<List<TicketResponse>> findAll() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<TicketResponse> responses = ticketService.findAll(userId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Endpoint para buscar um ticket por ID
     * GET /tickets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> findById(@PathVariable Long id) {
        Long userId = SecurityUtil.getCurrentUserId();
        TicketResponse response = ticketService.findById(id, userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para atualizar o status de um ticket
     * PUT /tickets/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody StatusUpdateRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        TicketResponse response = ticketService.updateStatus(id, request, userId);
        return ResponseEntity.ok(response);
    }
}

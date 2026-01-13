package com.helpdesk.controller;

import com.helpdesk.dto.request.CommentRequest;
import com.helpdesk.dto.response.CommentResponse;
import com.helpdesk.security.SecurityUtil;
import com.helpdesk.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações com comentários
 */
@RestController
@RequestMapping("/tickets/{ticketId}/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * Endpoint para criar um novo comentário em um ticket
     * POST /tickets/{ticketId}/comments
     */
    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @PathVariable Long ticketId,
            @Valid @RequestBody CommentRequest request) {
        Long userId = SecurityUtil.getCurrentUserId();
        CommentResponse response = commentService.create(ticketId, request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para listar todos os comentários de um ticket
     * GET /tickets/{ticketId}/comments
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAll(@PathVariable Long ticketId) {
        List<CommentResponse> responses = commentService.findAllByTicket(ticketId);
        return ResponseEntity.ok(responses);
    }
}

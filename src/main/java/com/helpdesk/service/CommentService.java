package com.helpdesk.service;

import com.helpdesk.dto.request.CommentRequest;
import com.helpdesk.dto.response.CommentResponse;
import com.helpdesk.dto.response.UserResponse;
import com.helpdesk.entity.Comment;
import com.helpdesk.entity.Ticket;
import com.helpdesk.entity.User;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.repository.CommentRepository;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações com comentários
 */
@Service
@Transactional
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Cria um novo comentário em um ticket
     * @param ticketId ID do ticket
     * @param request Dados do comentário
     * @param userId ID do usuário que está comentando
     * @return Comentário criado
     */
    public CommentResponse create(Long ticketId, CommentRequest request, Long userId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Comment comment = new Comment();
        comment.setTexto(request.getTexto());
        comment.setTicket(ticket);
        comment.setUsuario(user);

        Comment savedComment = commentRepository.save(comment);
        return toCommentResponse(savedComment);
    }

    /**
     * Busca todos os comentários de um ticket
     * @param ticketId ID do ticket
     * @return Lista de comentários
     */
    @Transactional(readOnly = true)
    public List<CommentResponse> findAllByTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado"));

        List<Comment> comments = commentRepository.findByTicketOrderByCreatedAtAsc(ticket);
        return comments.stream()
                .map(this::toCommentResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converte entidade Comment para DTO CommentResponse
     * @param comment Entidade Comment
     * @return DTO CommentResponse
     */
    private CommentResponse toCommentResponse(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.setId(comment.getId());
        response.setTexto(comment.getTexto());
        response.setTicketId(comment.getTicket().getId());
        response.setCreatedAt(comment.getCreatedAt());

        UserResponse userResponse = new UserResponse();
        userResponse.setId(comment.getUsuario().getId());
        userResponse.setEmail(comment.getUsuario().getEmail());
        userResponse.setNome(comment.getUsuario().getNome());
        userResponse.setPerfil(comment.getUsuario().getPerfil());
        response.setUsuario(userResponse);

        return response;
    }
}

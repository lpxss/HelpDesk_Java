package com.helpdesk.repository;

import com.helpdesk.entity.Comment;
import com.helpdesk.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações com a entidade Comment
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    /**
     * Busca todos os comentários de um ticket
     * @param ticket Ticket dos comentários
     * @return Lista de comentários do ticket
     */
    List<Comment> findByTicket(Ticket ticket);

    /**
     * Busca comentários ordenados por data de criação (mais antigos primeiro)
     * @param ticket Ticket dos comentários
     * @return Lista de comentários ordenados
     */
    List<Comment> findByTicketOrderByCreatedAtAsc(Ticket ticket);
}

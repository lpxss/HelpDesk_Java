package com.helpdesk.repository;

import com.helpdesk.entity.Ticket;
import com.helpdesk.entity.User;
import com.helpdesk.entity.enums.Prioridade;
import com.helpdesk.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository para operações com a entidade Ticket
 */
@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Busca todos os tickets de um usuário
     * @param usuario Usuário dono dos tickets
     * @return Lista de tickets do usuário
     */
    List<Ticket> findByUsuario(User usuario);

    /**
     * Busca todos os tickets atribuídos a um atendente
     * @param atendente Atendente responsável pelos tickets
     * @return Lista de tickets do atendente
     */
    List<Ticket> findByAtendente(User atendente);

    /**
     * Busca tickets por status
     * @param status Status dos tickets
     * @return Lista de tickets com o status informado
     */
    List<Ticket> findByStatus(Status status);

    /**
     * Busca tickets por prioridade
     * @param prioridade Prioridade dos tickets
     * @return Lista de tickets com a prioridade informada
     */
    List<Ticket> findByPrioridade(Prioridade prioridade);

    /**
     * Busca tickets por status e prioridade
     * @param status Status dos tickets
     * @param prioridade Prioridade dos tickets
     * @return Lista de tickets com o status e prioridade informados
     */
    List<Ticket> findByStatusAndPrioridade(Status status, Prioridade prioridade);
}

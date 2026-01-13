package com.helpdesk.service;

import com.helpdesk.dto.request.StatusUpdateRequest;
import com.helpdesk.dto.request.TicketRequest;
import com.helpdesk.dto.response.TicketResponse;
import com.helpdesk.dto.response.UserResponse;
import com.helpdesk.entity.Ticket;
import com.helpdesk.entity.User;
import com.helpdesk.entity.enums.Perfil;
import com.helpdesk.entity.enums.Status;
import com.helpdesk.exception.BusinessException;
import com.helpdesk.exception.ResourceNotFoundException;
import com.helpdesk.exception.UnauthorizedException;
import com.helpdesk.repository.TicketRepository;
import com.helpdesk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para operações com tickets
 */
@Service
@Transactional
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Cria um novo ticket
     * @param request Dados do ticket
     * @param userId ID do usuário criador
     * @return Ticket criado
     */
    public TicketResponse create(TicketRequest request, Long userId) {
        User usuario = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        Ticket ticket = new Ticket();
        ticket.setTitulo(request.getTitulo());
        ticket.setDescricao(request.getDescricao());
        ticket.setCategoria(request.getCategoria());
        ticket.setPrioridade(request.getPrioridade());
        ticket.setStatus(Status.ABERTO);
        ticket.setUsuario(usuario);

        Ticket savedTicket = ticketRepository.save(ticket);
        return toTicketResponse(savedTicket);
    }

    /**
     * Busca todos os tickets conforme perfil do usuário
     * @param userId ID do usuário
     * @return Lista de tickets
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> findAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        List<Ticket> tickets;

        if (user.getPerfil() == Perfil.ADMIN) {
            // ADMIN vê todos os tickets
            tickets = ticketRepository.findAll();
        } else if (user.getPerfil() == Perfil.SUPORTE) {
            // SUPORTE vê tickets atribuídos a ele
            tickets = ticketRepository.findByAtendente(user);
        } else {
            // USUARIO vê apenas seus próprios tickets
            tickets = ticketRepository.findByUsuario(user);
        }

        return tickets.stream()
                .map(this::toTicketResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca um ticket por ID
     * @param id ID do ticket
     * @param userId ID do usuário que está buscando
     * @return Ticket encontrado
     */
    @Transactional(readOnly = true)
    public TicketResponse findById(Long id, Long userId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Verifica permissão de acesso
        if (user.getPerfil() != Perfil.ADMIN) {
            if (user.getPerfil() == Perfil.SUPORTE) {
                // SUPORTE só vê tickets atribuídos a ele
                if (ticket.getAtendente() == null || !ticket.getAtendente().getId().equals(userId)) {
                    throw new UnauthorizedException("Você não tem permissão para acessar este ticket");
                }
            } else {
                // USUARIO só vê seus próprios tickets
                if (!ticket.getUsuario().getId().equals(userId)) {
                    throw new UnauthorizedException("Você não tem permissão para acessar este ticket");
                }
            }
        }

        return toTicketResponse(ticket);
    }

    /**
     * Atualiza o status de um ticket
     * @param id ID do ticket
     * @param request Dados da atualização
     * @param userId ID do usuário que está atualizando
     * @return Ticket atualizado
     */
    public TicketResponse updateStatus(Long id, StatusUpdateRequest request, Long userId) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket não encontrado"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        // Verifica permissão: apenas SUPORTE ou ADMIN pode mudar status
        if (user.getPerfil() != Perfil.SUPORTE && user.getPerfil() != Perfil.ADMIN) {
            throw new UnauthorizedException("Apenas SUPORTE ou ADMIN podem alterar o status do ticket");
        }

        Status novoStatus = request.getStatus();

        // Regras de negócio para mudança de status
        if (ticket.getStatus() == Status.FECHADO) {
            throw new BusinessException("Ticket fechado não pode ser reaberto");
        }

        if (novoStatus == Status.FECHADO && ticket.getStatus() != Status.RESOLVIDO) {
            throw new BusinessException("Ticket deve estar RESOLVIDO antes de ser FECHADO");
        }

        // Atualiza status
        ticket.setStatus(novoStatus);

        // Se está em atendimento, atribui ao usuário que está atualizando
        if (novoStatus == Status.EM_ATENDIMENTO && ticket.getAtendente() == null) {
            ticket.setAtendente(user);
        }

        Ticket updatedTicket = ticketRepository.save(ticket);
        return toTicketResponse(updatedTicket);
    }

    /**
     * Converte entidade Ticket para DTO TicketResponse
     * @param ticket Entidade Ticket
     * @return DTO TicketResponse
     */
    private TicketResponse toTicketResponse(Ticket ticket) {
        TicketResponse response = new TicketResponse();
        response.setId(ticket.getId());
        response.setTitulo(ticket.getTitulo());
        response.setDescricao(ticket.getDescricao());
        response.setCategoria(ticket.getCategoria());
        response.setPrioridade(ticket.getPrioridade());
        response.setStatus(ticket.getStatus());
        response.setCreatedAt(ticket.getCreatedAt());
        response.setUpdatedAt(ticket.getUpdatedAt());

        UserResponse usuarioResponse = new UserResponse();
        usuarioResponse.setId(ticket.getUsuario().getId());
        usuarioResponse.setEmail(ticket.getUsuario().getEmail());
        usuarioResponse.setNome(ticket.getUsuario().getNome());
        usuarioResponse.setPerfil(ticket.getUsuario().getPerfil());
        response.setUsuario(usuarioResponse);

        if (ticket.getAtendente() != null) {
            UserResponse atendenteResponse = new UserResponse();
            atendenteResponse.setId(ticket.getAtendente().getId());
            atendenteResponse.setEmail(ticket.getAtendente().getEmail());
            atendenteResponse.setNome(ticket.getAtendente().getNome());
            atendenteResponse.setPerfil(ticket.getAtendente().getPerfil());
            response.setAtendente(atendenteResponse);
        }

        return response;
    }
}

package com.helpdesk.repository;

import com.helpdesk.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para operações com a entidade User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário por email
     * @param email Email do usuário
     * @return Optional com o usuário encontrado
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica se existe um usuário com o email informado
     * @param email Email do usuário
     * @return true se existir, false caso contrário
     */
    boolean existsByEmail(String email);
}

package com.helpdesk.exception;

/**
 * Exceção lançada quando uma operação não autorizada é tentada
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}

package br.insper.insperMind.usuario.exception;

public class UsuarioAlreadyExistsException extends RuntimeException {
    public UsuarioAlreadyExistsException(String message) {
        super(message);
    }
}

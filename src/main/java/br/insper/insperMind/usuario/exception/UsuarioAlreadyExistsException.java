package br.insper.insperMind.usuario.exception;

public class UsuarioAlreadyExistsException extends RuntimeException {
    public UsuarioAlreadyExistsException() {
        super("Usuario já cadastrado");
    }
}
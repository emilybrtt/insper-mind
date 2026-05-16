package br.insper.insperMind.usuario.exception;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException() {
        super("Usuario nao encontrado");
    }
}
package br.insper.insperMind.usuario.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Email ou senha inválidos");
    }
}
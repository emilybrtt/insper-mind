package br.insper.insperMind.forumPost.exception;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException() {
        super("Acesso negado");
    }
}
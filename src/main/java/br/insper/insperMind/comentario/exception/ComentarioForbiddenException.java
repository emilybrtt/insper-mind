package br.insper.insperMind.comentario.exception;

public class ComentarioForbiddenException extends RuntimeException {
    public ComentarioForbiddenException() {
        super("Você não tem permissão para alterar este comentário");
    }
}
package br.insper.insperMind.comentario.exception;

public class ComentarioNotFoundException extends RuntimeException {
    public ComentarioNotFoundException() {
        super("Comentario nao encontrado");
    }
}
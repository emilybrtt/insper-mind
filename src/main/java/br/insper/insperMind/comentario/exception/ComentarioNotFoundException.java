package br.insper.insperMind.comentario.exception;

public class ComentarioNotFoundException extends RuntimeException {

    public ComentarioNotFoundException(String message) {
        super("Comentario nao encontrado");
    }
}
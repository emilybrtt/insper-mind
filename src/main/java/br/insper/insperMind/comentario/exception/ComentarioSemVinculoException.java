package br.insper.insperMind.comentario.exception;

public class ComentarioSemVinculoException extends RuntimeException {
    public ComentarioSemVinculoException() {
        super("Comentario deve ter disciplina ou material");
    }
}
package br.insper.insperMind.curso.exception;

public class CursoAlreadyExistsException extends RuntimeException {
    public CursoAlreadyExistsException(String message) {
        super(message);
    }
}

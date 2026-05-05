package br.insper.insperMind.semestre.exception;

public class SemestreAlreadyExistsException extends RuntimeException {
    public SemestreAlreadyExistsException(String message) {
        super(message);
    }
}

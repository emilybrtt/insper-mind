package br.insper.insperMind.semestre.exception;

public class SemestreNotFoundException extends RuntimeException {
    public SemestreNotFoundException(String message) {
        super(message);
    }
}

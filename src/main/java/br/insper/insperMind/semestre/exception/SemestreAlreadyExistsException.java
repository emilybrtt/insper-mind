package br.insper.insperMind.semestre.exception;

public class SemestreAlreadyExistsException extends RuntimeException {
    public SemestreAlreadyExistsException() {
        super("Semestre já cadastrado");
    }
}

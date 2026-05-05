package br.insper.insperMind.docente.exception;

public class DocenteAlreadyExistsException extends RuntimeException {
    public DocenteAlreadyExistsException(String message) {
        super(message);
    }
}

package br.insper.insperMind.eletiva.exception;

public class EletivaAlreadyExistsException extends RuntimeException {
    public EletivaAlreadyExistsException() {
        super("Eletiva já cadastrada");
    }
}
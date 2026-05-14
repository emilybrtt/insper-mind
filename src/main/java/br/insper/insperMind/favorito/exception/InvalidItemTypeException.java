package br.insper.insperMind.favorito.exception;

public class InvalidItemTypeException extends RuntimeException {
    public InvalidItemTypeException() {
        super("Tipo de item invalido");
    }
}
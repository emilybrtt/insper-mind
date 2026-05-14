package br.insper.insperMind.material.exception;

public class MaterialInvalidTypeException extends RuntimeException {
    public MaterialInvalidTypeException() {
        super("Tipo de material invalido");
    }
}
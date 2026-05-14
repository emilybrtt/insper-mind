package br.insper.insperMind.material.exception;

public class MaterialAlreadyExistsException extends RuntimeException {
    public MaterialAlreadyExistsException() {
        super("Material já cadastrado");
    }
}
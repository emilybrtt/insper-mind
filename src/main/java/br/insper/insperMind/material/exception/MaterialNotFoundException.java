package br.insper.insperMind.material.exception;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException() {
        super("Material nao encontrado");
    }
}
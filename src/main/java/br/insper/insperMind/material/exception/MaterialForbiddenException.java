package br.insper.insperMind.material.exception;

public class MaterialForbiddenException extends RuntimeException {
    public MaterialForbiddenException() {
        super("Apenas o criador do material pode edita-lo ou deleta-lo");
    }
}
package br.insper.insperMind.material.exception;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException() {
        super("Arquivo inválido ou vazio");
    }
}
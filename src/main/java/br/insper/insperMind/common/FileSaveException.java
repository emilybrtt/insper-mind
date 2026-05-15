package br.insper.insperMind.common;

public class FileSaveException extends RuntimeException {
    public FileSaveException() {
        super("Erro ao salvar arquivo");
    }
}

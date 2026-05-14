package br.insper.insperMind.docente.exception;

public class DocenteNotFoundException extends RuntimeException {
    public DocenteNotFoundException() {
        super("Docente nao encontrado");
    }
}
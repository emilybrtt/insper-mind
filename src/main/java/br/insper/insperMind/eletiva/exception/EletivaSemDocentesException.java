package br.insper.insperMind.eletiva.exception;

public class EletivaSemDocentesException extends RuntimeException {
    public EletivaSemDocentesException() {
        super("Eletiva deve ter ao menos um docente");
    }
}
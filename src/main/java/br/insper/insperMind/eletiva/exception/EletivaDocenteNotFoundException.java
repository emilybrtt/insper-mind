package br.insper.insperMind.eletiva.exception;

public class EletivaDocenteNotFoundException extends RuntimeException {
    public EletivaDocenteNotFoundException() {
        super("Um ou mais docentes nao encontrados");
    }
}
package br.insper.insperMind.disciplina.exception;

public class DisciplinaDocenteNotFoundException extends RuntimeException {
    public DisciplinaDocenteNotFoundException() {
        super("Um ou mais docentes nao encontrados");
    }
}
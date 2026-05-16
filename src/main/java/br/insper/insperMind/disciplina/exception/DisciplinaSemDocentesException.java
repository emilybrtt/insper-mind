package br.insper.insperMind.disciplina.exception;

public class DisciplinaSemDocentesException extends RuntimeException {
    public DisciplinaSemDocentesException() {
        super("Disciplina deve ter ao menos um docente");
    }
}
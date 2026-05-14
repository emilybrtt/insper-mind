package br.insper.insperMind.disciplina.exception;

public class DisciplinaAlreadyExistsException extends RuntimeException {
    public DisciplinaAlreadyExistsException() {
        super("Disciplina já cadastrada");
    }
}
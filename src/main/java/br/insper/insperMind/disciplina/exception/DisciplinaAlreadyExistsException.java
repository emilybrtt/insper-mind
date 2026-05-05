package br.insper.insperMind.disciplina.exception;

public class DisciplinaAlreadyExistsException extends RuntimeException {
    public DisciplinaAlreadyExistsException(String message) {
        super(message);
    }
}

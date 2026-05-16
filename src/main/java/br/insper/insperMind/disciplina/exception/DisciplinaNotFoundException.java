package br.insper.insperMind.disciplina.exception;

public class DisciplinaNotFoundException extends RuntimeException {
    public DisciplinaNotFoundException() {
        super("Disciplina nao encontrada");
    }
}
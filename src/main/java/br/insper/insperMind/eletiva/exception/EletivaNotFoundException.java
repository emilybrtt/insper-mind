package br.insper.insperMind.eletiva.exception;

public class EletivaNotFoundException extends RuntimeException {
    public EletivaNotFoundException() {
        super("Eletiva nao encontrada");
    }
}
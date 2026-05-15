package br.insper.insperMind.favorito.exception;

public class FavoritoForbiddenException extends RuntimeException {
    public FavoritoForbiddenException() {
        super("Acesso negado");
    }
}
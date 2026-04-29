package br.insper.insperMind.favorito.exception;

public class FavoritoNotFoundException extends RuntimeException {
    public FavoritoNotFoundException(String message) {
        super(message);
    }
}

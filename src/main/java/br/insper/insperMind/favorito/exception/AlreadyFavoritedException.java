package br.insper.insperMind.favorito.exception;

public class AlreadyFavoritedException extends RuntimeException {
    public AlreadyFavoritedException(String message) {
        super(message);
    }
}
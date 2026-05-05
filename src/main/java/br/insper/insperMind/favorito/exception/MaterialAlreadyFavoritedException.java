package br.insper.insperMind.favorito.exception;

public class MaterialAlreadyFavoritedException extends RuntimeException {
    public MaterialAlreadyFavoritedException(String message) {
        super(message);
    }
}

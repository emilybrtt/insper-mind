package br.insper.insperMind.favorito.exception;

public class MaterialAlreadyFavoritedException extends RuntimeException {
    public MaterialAlreadyFavoritedException() {
        super("Material já favoritado");
    }
}
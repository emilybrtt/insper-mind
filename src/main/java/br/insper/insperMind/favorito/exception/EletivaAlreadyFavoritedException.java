package br.insper.insperMind.favorito.exception;

public class EletivaAlreadyFavoritedException extends RuntimeException {
    public EletivaAlreadyFavoritedException() {
        super("Eletiva já favoritada");
    }
}
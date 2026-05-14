package br.insper.insperMind.favorito.exception;

public class FavoritoForbiddenException extends RuntimeException {
    public FavoritoForbiddenException() {
        super("Você não tem permissão para deletar este favorito");
    }
}
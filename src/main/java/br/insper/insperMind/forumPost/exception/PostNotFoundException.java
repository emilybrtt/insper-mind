package br.insper.insperMind.forumPost.exception;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException() {
        super("Post não encontrado");
    }
}
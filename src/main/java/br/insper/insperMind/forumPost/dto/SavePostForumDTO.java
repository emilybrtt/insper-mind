package br.insper.insperMind.forumPost.dto;
import jakarta.validation.constraints.NotBlank;
import br.insper.insperMind.forumPost.CategoriaForum;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SavePostForumDTO {
    @NotBlank private String titulo;
    @NotBlank private String conteudo;
    @NotNull
    private CategoriaForum categoria;

}
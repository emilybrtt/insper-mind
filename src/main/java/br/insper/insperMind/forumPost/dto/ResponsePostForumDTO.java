package br.insper.insperMind.forumPost.dto;

import br.insper.insperMind.forumPost.CategoriaForum;
import br.insper.insperMind.forumPost.PostForum;
import br.insper.insperMind.usuario.dto.ResponseUsuarioDTO;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ResponsePostForumDTO {
    private Integer id;
    private String titulo;
    private String conteudo;
    private ResponseUsuarioDTO usuario;
    private CategoriaForum categoria;
    private LocalDateTime dataCriacao;
    private Integer curtidas;

    public static ResponsePostForumDTO toDTO(PostForum post) {
        ResponsePostForumDTO dto = new ResponsePostForumDTO();
        dto.id = post.getId();
        dto.titulo = post.getTitulo();
        dto.conteudo = post.getConteudo();
        dto.usuario = ResponseUsuarioDTO.toDTO(post.getUsuario());
        dto.categoria = post.getCategoria();
        dto.dataCriacao = post.getDataCriacao();
        dto.curtidas = post.getCurtidas();
        return dto;
    }
}
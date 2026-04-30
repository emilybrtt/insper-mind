package br.insper.insperMind.comentario.dto;

import br.insper.insperMind.comentario.Comentario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseComentarioDTO {
    private Integer id;
    private String comentario;
    private Integer curtidas;
    private String nomeUsuario;
    private String emailUsuario;
    private LocalDateTime dataCriacao;

    public static ResponseComentarioDTO toDTO(Comentario comentario) {
        ResponseComentarioDTO dto = new ResponseComentarioDTO();
        dto.setId(comentario.getId());
        dto.setComentario(comentario.getComentario());
        dto.setCurtidas(comentario.getCurtidas());
        dto.setDataCriacao(comentario.getDataCriacao());

        if (comentario.getUsuario() != null) {
            dto.setNomeUsuario(comentario.getUsuario().getNome());
            dto.setEmailUsuario(comentario.getUsuario().getEmail());
        }

        return dto;
    }
}

package br.insper.insperMind.comentario.dto;

import br.insper.insperMind.comentario.Comentario;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ResponseComentarioDTO {
    private Integer id;
    private String comentario;
    private Integer curtidas;
    private String nomeUsuario;
    private String emailUsuario;
    private LocalDateTime dataCriacao;

    private Integer idDisciplina;
    private String nomeDisciplina;

    private Integer idMaterial;
    private String tituloMaterial;

    private Integer comentarioPaiId;
    private List<ResponseComentarioDTO> respostas;

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

        if (comentario.getDisciplina() != null) {
            dto.setIdDisciplina(comentario.getDisciplina().getId());
            dto.setNomeDisciplina(comentario.getDisciplina().getNome());
        }

        if (comentario.getMaterial() != null) {
            dto.setIdMaterial(comentario.getMaterial().getId());
            dto.setTituloMaterial(comentario.getMaterial().getTitulo());
        }

        if (comentario.getComentarioPai() != null) {
            dto.setComentarioPaiId(comentario.getComentarioPai().getId());
        }

        if (comentario.getRespostas() != null) {
            dto.setRespostas(
                    comentario.getRespostas()
                            .stream()
                            .map(ResponseComentarioDTO::toDTO)
                            .toList()
            );
        }

        return dto;
    }
}
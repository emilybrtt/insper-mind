package br.insper.insperMind.material.dto;

import br.insper.insperMind.material.Material;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseMaterialDTO {

    private Integer id;
    private String titulo;
    private String descricao;
    private String link;
    private String tipo;
    private String nomeUsuario;
    private String emailUsuario;
    private Integer cursoId;
    private String nomeCurso;
    private LocalDateTime dataCriacao;

    public static ResponseMaterialDTO toDTO(Material material) {
        ResponseMaterialDTO dto = new ResponseMaterialDTO();

        dto.setId(material.getId());
        dto.setTitulo(material.getTitulo());
        dto.setDescricao(material.getDescricao());
        dto.setLink(material.getLink());

        if (material.getTipo() != null) {
            dto.setTipo(material.getTipo().name());
        }

        if (material.getUsuario() != null) {
            dto.setNomeUsuario(material.getUsuario().getNome());
            dto.setEmailUsuario(material.getUsuario().getEmail());
        }

        if (material.getCurso() != null) {
            dto.setCursoId(material.getCurso().getId());
            dto.setNomeCurso(material.getCurso().getNome());
        }

        dto.setDataCriacao(material.getDataCriacao());

        return dto;
    }
}
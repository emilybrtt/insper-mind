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

        dto.setDataCriacao(material.getDataCriacao());

        if (material.getUsuario() != null) {
            dto.setNomeUsuario(material.getUsuario().getNome());
            dto.setEmailUsuario(material.getUsuario().getEmail());
        }

        return dto;
    }
}
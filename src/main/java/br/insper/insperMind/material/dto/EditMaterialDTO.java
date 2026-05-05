package br.insper.insperMind.material.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMaterialDTO {
    private String emailUsuario;
    private String titulo;
    private String descricao;
    private String link;
    private String tipo;
    private Integer disciplinaId;
    private Boolean ativo;
}

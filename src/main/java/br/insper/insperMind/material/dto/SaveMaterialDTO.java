package br.insper.insperMind.material.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveMaterialDTO {

    private String titulo;
    private String descricao;
    private String link;
    private String tipo;
    private String emailUsuario;
    private Integer cursoId;
}
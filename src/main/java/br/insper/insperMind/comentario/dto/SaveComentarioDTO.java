package br.insper.insperMind.comentario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveComentarioDTO {
    private String comentario;
    private Integer idDisciplina;
    private Integer idMaterial;
    private Integer comentarioPaiId;
}
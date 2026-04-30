package br.insper.insperMind.comentario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditComentarioDTO {
    private String comentario;
    private Boolean ativo;
}
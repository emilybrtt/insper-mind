package br.insper.insperMind.comentario.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveComentarioDTO {
    @NotBlank(message = "Comentário não pode ser vazio")
    @Size(max = 2000)
    private String comentario;
    private Integer idDisciplina;
    private Integer idMaterial;
    private Integer comentarioPaiId;
}
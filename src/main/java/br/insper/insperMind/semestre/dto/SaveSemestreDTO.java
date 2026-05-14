package br.insper.insperMind.semestre.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveSemestreDTO {

    @NotBlank(message = "Nome do semestre é obrigatório")
    private String nome;

    @NotNull(message = "Curso é obrigatório")
    private Integer cursoId;

}

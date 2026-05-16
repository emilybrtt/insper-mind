package br.insper.insperMind.curso.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditCursoDTO {
    @NotBlank(message = "Nome do curso é obrigatório")
    private String nome;
    private Boolean ativo;
}
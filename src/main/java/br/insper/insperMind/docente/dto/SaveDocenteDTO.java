package br.insper.insperMind.docente.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveDocenteDTO {
    @NotBlank(message = "Nome do docente é obrigatório")
    private String nome;

    @NotBlank(message = "Email do docente é obrigatório")
    @Email(message = "Email do docente inválido")
    private String email;
}
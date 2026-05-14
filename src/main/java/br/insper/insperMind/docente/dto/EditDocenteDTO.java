package br.insper.insperMind.docente.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDocenteDTO {
    private String nome;

    @Email(message = "Email do docente inválido")
    private String email;
}
package br.insper.insperMind.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditUsuarioDTO {
    private String nome;

    @Email(message = "Email do usuario inválido")
    private String email;

    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    private String senha;
}
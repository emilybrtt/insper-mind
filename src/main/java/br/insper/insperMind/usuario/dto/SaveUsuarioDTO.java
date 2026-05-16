package br.insper.insperMind.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUsuarioDTO {
    @NotBlank(message = "Nome do usuario é obrigatório")
    private String nome;

    @NotBlank(message = "Email do usuario é obrigatório")
    @Email(message = "Email do usuario inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 8, message = "Senha deve ter pelo menos 8 caracteres")
    private String senha;
}
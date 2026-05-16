package br.insper.insperMind.usuario.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetAtivoDTO {
    @NotNull
    Boolean ativo;
}

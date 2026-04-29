package br.insper.insperMind.usuario.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUsuarioDTO {
    private String nome;
    private String email;
    private String senha;

}

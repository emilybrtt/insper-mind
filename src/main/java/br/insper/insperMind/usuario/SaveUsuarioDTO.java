package br.insper.insperMind.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveUsuarioDTO {
    private String nome;
    private String email;
    private String senha;

}

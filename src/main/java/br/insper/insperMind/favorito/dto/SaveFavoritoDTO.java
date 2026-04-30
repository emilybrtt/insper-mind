package br.insper.insperMind.favorito.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFavoritoDTO {
    private String emailUsuario;
    private Integer itemId;
    private String tipoItem;
}
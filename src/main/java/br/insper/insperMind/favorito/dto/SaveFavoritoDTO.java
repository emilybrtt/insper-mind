package br.insper.insperMind.favorito.dto;

import br.insper.insperMind.favorito.TipoFavorito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFavoritoDTO {
    @NotBlank(message = "Email do usuario é obrigatório")
    private String emailUsuario;

    @NotNull(message = "Item é obrigatório")
    private Integer itemId;

    @NotNull(message = "Tipo do item é obrigatório")
    private TipoFavorito tipoItem;
}
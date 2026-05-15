package br.insper.insperMind.favorito.dto;

import br.insper.insperMind.favorito.TipoFavorito;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveFavoritoDTO {
    private Integer materialId;
    private Integer eletivaId;

}

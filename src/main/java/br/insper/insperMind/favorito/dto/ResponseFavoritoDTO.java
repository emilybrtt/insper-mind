package br.insper.insperMind.favorito.dto;

import br.insper.insperMind.favorito.Favorito;
import br.insper.insperMind.favorito.TipoFavorito;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseFavoritoDTO {

    private Integer id;
    private String nomeUsuario;
    private String emailUsuario;
    private Integer itemId;
    private TipoFavorito tipoItem;
    private LocalDateTime dataSalvo;
    private String tituloMaterial;
    private String nomeEletiva;

    public static ResponseFavoritoDTO toDTO(Favorito favorito) {
        ResponseFavoritoDTO dto = new ResponseFavoritoDTO();

        dto.setId(favorito.getId());
        dto.setDataSalvo(favorito.getDataSalvo());

        if (favorito.getMaterial() != null) {
            dto.setItemId(favorito.getMaterial().getId());
            dto.setTipoItem(TipoFavorito.MATERIAL);
            dto.setTituloMaterial(favorito.getMaterial().getTitulo());
        } else if (favorito.getEletiva() != null) {
            dto.setItemId(favorito.getEletiva().getId());
            dto.setTipoItem(TipoFavorito.ELETIVA);
            dto.setNomeEletiva(favorito.getEletiva().getNome());
        }


        if (favorito.getUsuario() != null) {
            dto.setNomeUsuario(favorito.getUsuario().getNome());
            dto.setEmailUsuario(favorito.getUsuario().getEmail());
        }

        return dto;
    }
}

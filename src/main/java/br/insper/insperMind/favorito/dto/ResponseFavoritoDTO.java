package br.insper.insperMind.favorito.dto;

import br.insper.insperMind.favorito.Favorito;
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
    private String tipoItem;
    private LocalDateTime dataSalvo;

    public static ResponseFavoritoDTO toDTO(Favorito favorito) {
        ResponseFavoritoDTO dto = new ResponseFavoritoDTO();

        dto.setId(favorito.getId());
        dto.setDataSalvo(favorito.getDataSalvo());

        if (favorito.getMaterial() != null) {
            dto.setItemId(favorito.getMaterial().getId());
            dto.setTipoItem("MATERIAL");
         }else if (favorito.getEletiva() != null) {
            dto.setItemId(favorito.getEletiva().getId());
            dto.setTipoItem("ELETIVA");
        }


        if (favorito.getUsuario() != null) {
            dto.setNomeUsuario(favorito.getUsuario().getNome());
            dto.setEmailUsuario(favorito.getUsuario().getEmail());
        }

        return dto;
    }
}

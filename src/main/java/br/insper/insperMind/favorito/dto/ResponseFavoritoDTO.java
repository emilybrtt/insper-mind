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
        dto.setItemId(favorito.getItemId());
        dto.setTipoItem(favorito.getTipoItem());
        dto.setDataSalvo(favorito.getDataSalvo());

        if (favorito.getUsuario() != null) {
            dto.setNomeUsuario(favorito.getUsuario().getNome());
            dto.setEmailUsuario(favorito.getUsuario().getEmail());
        }

        return dto;
    }
}

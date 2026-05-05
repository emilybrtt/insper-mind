package br.insper.insperMind.eletiva.dto;

import br.insper.insperMind.eletiva.Eletiva;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseEletivaDTO {
    private Integer cargaHoraria;
    private String semestreMinimo;
    private Boolean ativo;
    private String nome;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;

    public static @org.jspecify.annotations.NonNull ResponseEletivaDTO toDTO(Eletiva eletiva) {
        ResponseEletivaDTO responseEletivaDTO = new ResponseEletivaDTO();
        responseEletivaDTO.setCargaHoraria(eletiva.getCargaHoraria());
        responseEletivaDTO.setSemestreMinimo(eletiva.getSemestreMinimo());
        responseEletivaDTO.setAtivo(eletiva.getAtivo());

        return responseEletivaDTO;
    }
}

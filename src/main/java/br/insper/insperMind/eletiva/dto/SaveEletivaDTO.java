package br.insper.insperMind.eletiva.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveEletivaDTO {
    private Integer cargaHoraria;
    private String semestreMinimo;
    private Boolean ativo;
}

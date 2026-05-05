package br.insper.insperMind.eletiva.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditEletivaDTO {
    private Integer cargaHoraria;
    private String semestreMinimo;
    private String nome;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;
}

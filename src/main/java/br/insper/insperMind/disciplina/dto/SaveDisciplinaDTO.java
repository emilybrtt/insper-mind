package br.insper.insperMind.disciplina.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveDisciplinaDTO {
    private String nome;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;
    private Integer semestreId;
    private List<Integer> docenteIds;
}
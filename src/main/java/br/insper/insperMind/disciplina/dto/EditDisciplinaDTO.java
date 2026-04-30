package br.insper.insperMind.disciplina.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditDisciplinaDTO {
    private String nome;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;
}
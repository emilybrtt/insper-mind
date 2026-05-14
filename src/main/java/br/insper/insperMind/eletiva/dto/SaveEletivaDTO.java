package br.insper.insperMind.eletiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveEletivaDTO {

    @NotNull(message = "Carga horaria é obrigatória")
    private Integer cargaHoraria;

    @NotBlank(message = "Semestre minimo é obrigatório")
    private Integer semestreMinimo;

    @NotBlank(message = "Nome da eletiva é obrigatório")
    private String nome;

    @NotBlank(message = "Formula de avaliacao é obrigatória")
    private String formulaAvaliacao;

    @NotNull(message = "Tem delta é obrigatório")
    private Boolean temDelta;

    @NotBlank(message = "Criterio de barreira é obrigatório")
    private String criterioBarreira;

    @NotNull(message = "A eletiva deve ter pelo menos um docente")
    private List<Integer> docenteIds;
}
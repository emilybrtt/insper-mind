package br.insper.insperMind.eletiva.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveEletivaDTO {

    @NotNull(message = "Carga horaria é obrigatória")
    @Min(value = 1, message = "Carga horária deve ser maior que 0")
    private Integer cargaHoraria;

    @NotNull(message = "Semestre minimo é obrigatório")
    @Min(value = 1, message = "Semestre mínimo deve ser maior que 0")
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
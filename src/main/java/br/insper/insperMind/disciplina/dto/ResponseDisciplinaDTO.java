package br.insper.insperMind.disciplina.dto;

import br.insper.insperMind.disciplina.Disciplina;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseDisciplinaDTO {

    private Integer id;
    private String nome;
    private LocalDateTime dataAtualizacao;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;

    public static ResponseDisciplinaDTO toDTO(Disciplina disciplina) {
        ResponseDisciplinaDTO dto = new ResponseDisciplinaDTO();

        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setDataAtualizacao(disciplina.getDataAtualizacao());
        dto.setFormulaAvaliacao(disciplina.getFormulaAvaliacao());
        dto.setTemDelta(disciplina.getTemDelta());
        dto.setCriterioBarreira(disciplina.getCriterioBarreira());

        return dto;
    }
}
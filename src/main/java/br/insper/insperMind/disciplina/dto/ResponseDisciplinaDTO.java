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
    private Integer semestreId;
    private String nomeSemestre;
    private Integer cursoId;
    private String nomeCurso;

    public static ResponseDisciplinaDTO toDTO(Disciplina disciplina) {
        ResponseDisciplinaDTO dto = new ResponseDisciplinaDTO();

        dto.setId(disciplina.getId());
        dto.setNome(disciplina.getNome());
        dto.setDataAtualizacao(disciplina.getDataAtualizacao());
        dto.setFormulaAvaliacao(disciplina.getFormulaAvaliacao());
        dto.setTemDelta(disciplina.getTemDelta());
        dto.setCriterioBarreira(disciplina.getCriterioBarreira());
        if (disciplina.getSemestre() != null) {
            dto.setSemestreId(disciplina.getSemestre().getId());
            dto.setNomeSemestre(disciplina.getSemestre().getNome());
            if (disciplina.getSemestre().getCurso() != null) {
                dto.setCursoId(disciplina.getSemestre().getCurso().getId());
                dto.setNomeCurso(disciplina.getSemestre().getCurso().getNome());
            }
        }

        return dto;
    }
}

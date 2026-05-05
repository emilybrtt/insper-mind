package br.insper.insperMind.semestre.dto;

import br.insper.insperMind.semestre.Semestre;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseSemestreDTO {

    private Integer id;
    private String nome;
    private Boolean ativo;
    private Integer cursoId;
    private String nomeCurso;

    public static ResponseSemestreDTO toDTO(Semestre semestre) {
        ResponseSemestreDTO dto = new ResponseSemestreDTO();

        dto.setId(semestre.getId());
        dto.setNome(semestre.getNome());
        dto.setAtivo(semestre.getAtivo());
        if (semestre.getCurso() != null) {
            dto.setCursoId(semestre.getCurso().getId());
            dto.setNomeCurso(semestre.getCurso().getNome());
        }

        return dto;
    }
}

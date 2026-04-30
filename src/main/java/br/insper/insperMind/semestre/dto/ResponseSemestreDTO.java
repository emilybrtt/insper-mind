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

    public static ResponseSemestreDTO toDTO(Semestre semestre) {
        ResponseSemestreDTO dto = new ResponseSemestreDTO();

        dto.setId(semestre.getId());
        dto.setNome(semestre.getNome());
        dto.setAtivo(semestre.getAtivo());

        return dto;
    }
}
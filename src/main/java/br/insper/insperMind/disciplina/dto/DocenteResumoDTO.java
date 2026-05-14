package br.insper.insperMind.disciplina.dto;

import br.insper.insperMind.docente.Docente;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocenteResumoDTO {
    private Integer id;
    private String nome;
    private String email;

    public static DocenteResumoDTO toDTO(Docente docente) {
        DocenteResumoDTO dto = new DocenteResumoDTO();
        dto.setId(docente.getId());
        dto.setNome(docente.getNome());
        dto.setEmail(docente.getEmail());
        return dto;
    }
}
package br.insper.insperMind.curso.dto;

import br.insper.insperMind.curso.Curso;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseCursoDTO {

    private Integer id;
    private String nome;

    public static ResponseCursoDTO toDTO(Curso curso) {
        ResponseCursoDTO dto = new ResponseCursoDTO();

        dto.setId(curso.getId());
        dto.setNome(curso.getNome());

        return dto;
    }
}
package br.insper.insperMind.docente.dto;

import br.insper.insperMind.docente.Docente;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseDocenteDTO {
    private Integer id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static ResponseDocenteDTO toDTO(Docente docente) {
        ResponseDocenteDTO dto = new ResponseDocenteDTO();
        dto.setId(docente.getId());
        dto.setNome(docente.getNome());
        dto.setEmail(docente.getEmail());
        dto.setDataCriacao(docente.getDataCriacao());
        dto.setDataAtualizacao(docente.getDataAtualizacao());
        return dto;
    }
}
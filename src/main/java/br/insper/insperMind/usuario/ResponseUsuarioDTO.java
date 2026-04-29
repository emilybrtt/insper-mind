package br.insper.insperMind.usuario;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResponseUsuarioDTO {
    private Integer id;
    private String nome;
    private String email;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public static ResponseUsuarioDTO toDTO(Usuario usuario) {
        ResponseUsuarioDTO dto = new ResponseUsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setDataCriacao(usuario.getDataCriacao());
        dto.setDataAtualizacao(usuario.getDataAtualizacao());
        return dto;
    }
}
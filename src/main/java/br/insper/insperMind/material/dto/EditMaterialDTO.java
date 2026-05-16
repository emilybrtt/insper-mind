package br.insper.insperMind.material.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditMaterialDTO {
    @NotBlank(message = "Titulo é obrigatório")
    private String titulo;

    @NotBlank(message = "Descricao é obrigatória")
    private String descricao;

    @NotBlank(message = "Link é obrigatório")
    private String link;

    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;

    private Integer disciplinaId;
    private Boolean ativo;
}
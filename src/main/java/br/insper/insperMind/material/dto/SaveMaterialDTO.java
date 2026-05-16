package br.insper.insperMind.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveMaterialDTO {

    @NotBlank(message = "Titulo é obrigatório")
    private String titulo;

    @NotBlank(message = "Descricao é obrigatória")
    private String descricao;

    @NotBlank(message = "Link é obrigatório")
    @Pattern(regexp = "^(https?://|/).*", message = "Link deve ser uma URL válida")
    private String link;

    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;

    @NotNull(message = "Disciplina é obrigatória")
    private Integer disciplinaId;
}

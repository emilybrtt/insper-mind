package br.insper.insperMind.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LandingPageDTO {
    private String titulo;
    private String descricao;
    private String[] features;
}
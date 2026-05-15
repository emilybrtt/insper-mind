package br.insper.insperMind.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import br.insper.insperMind.common.LandingPageDTO;

@RestController
public class LandingPageController {

    @GetMapping("/")
    public LandingPageDTO getLandingPage() {
        LandingPageDTO dto = new LandingPageDTO();
        dto.setTitulo("Insper Mind");
        dto.setDescricao("Portal acadêmico com cursos, materiais e contatos");
        dto.setFeatures(new String[]{"Estrutura de Cursos", "Acervo de Materiais", "Portal de Contatos"}
        );
        return dto;
    }
}
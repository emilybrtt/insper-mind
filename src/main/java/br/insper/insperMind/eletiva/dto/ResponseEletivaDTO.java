package br.insper.insperMind.eletiva.dto;

import br.insper.insperMind.docente.dto.DocenteResumoDTO;
import br.insper.insperMind.eletiva.Eletiva;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResponseEletivaDTO {
    private Integer id;
    private Integer cargaHoraria;
    private Integer semestreMinimo;
    private Boolean ativo;
    private String nome;
    private String formulaAvaliacao;
    private Boolean temDelta;
    private String criterioBarreira;
    private List<DocenteResumoDTO> docentes;

    public static ResponseEletivaDTO toDTO(Eletiva eletiva) {
        ResponseEletivaDTO dto = new ResponseEletivaDTO();
        dto.setId(eletiva.getId());
        dto.setCargaHoraria(eletiva.getCargaHoraria());
        dto.setSemestreMinimo(eletiva.getSemestreMinimo());
        dto.setAtivo(eletiva.getAtivo());
        dto.setNome(eletiva.getNome());
        dto.setFormulaAvaliacao(eletiva.getFormulaAvaliacao());
        dto.setTemDelta(eletiva.getTemDelta());
        dto.setCriterioBarreira(eletiva.getCriterioBarreira());

        if (eletiva.getDocentes() != null) {
            dto.setDocentes(
                    eletiva.getDocentes().stream()
                            .map(DocenteResumoDTO::toDTO)
                            .toList()
            );
        }

        return dto;
    }
}
package br.insper.insperMind.eletiva;

import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("Eletiva")
public class Eletiva extends Disciplina {

    @Column(nullable = false)
    private Integer cargaHoraria;

    @Column(nullable = false)
    private String semestreMinimo;

    public static @NonNull Eletiva toModel(SaveEletivaDTO saveEletivaDTO) {
        Eletiva eletiva = new Eletiva();
        eletiva.setCargaHoraria(saveEletivaDTO.getCargaHoraria());
        eletiva.setSemestreMinimo(saveEletivaDTO.getSemestreMinimo());
        eletiva.setAtivo(saveEletivaDTO.getAtivo() != null ? saveEletivaDTO.getAtivo() : true);

        eletiva.setNome(saveEletivaDTO.getNome());
        eletiva.setFormulaAvaliacao(saveEletivaDTO.getFormulaAvaliacao());
        eletiva.setCriterioBarreira(saveEletivaDTO.getCriterioBarreira());
        eletiva.setTemDelta(saveEletivaDTO.getTemDelta());
        eletiva.setSemestre(null);

        return eletiva;
        }
    }


package br.insper.insperMind.eletiva;

import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ELETIVA")
public class Eletiva extends Disciplina {

    @Column(nullable = false)
    private Integer cargaHoraria;

    @Column(nullable = false)
    private Integer semestreMinimo;

    public static Eletiva toModel(SaveEletivaDTO dto) {
        Eletiva eletiva = new Eletiva();
        eletiva.setCargaHoraria(dto.getCargaHoraria());
        eletiva.setSemestreMinimo(dto.getSemestreMinimo());
        eletiva.setNome(dto.getNome());
        eletiva.setFormulaAvaliacao(dto.getFormulaAvaliacao());
        eletiva.setTemDelta(dto.getTemDelta());
        eletiva.setCriterioBarreira(dto.getCriterioBarreira());
        eletiva.setAtivo(true);
        eletiva.setSemestre(null);
        return eletiva;
    }
}
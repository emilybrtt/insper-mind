package br.insper.insperMind.eletiva;

import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Eletiva extends Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer cargaHoraria;

    @Column(nullable = false)
    private String semestreMinimo;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo;

    public static @NonNull Eletiva toModel(SaveEletivaDTO saveEletivaDTO) {
        Eletiva eletiva = new Eletiva();
        eletiva.setCargaHoraria(saveEletivaDTO.getCargaHoraria());
        eletiva.setSemestreMinimo(saveEletivaDTO.getSemestreMinimo());
        if (saveEletivaDTO.getAtivo() != null) {
            eletiva.setAtivo(saveEletivaDTO.getAtivo());
        }

        return eletiva;
    }

}
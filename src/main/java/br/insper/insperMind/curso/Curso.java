package br.insper.insperMind.curso;

import br.insper.insperMind.semestre.Semestre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String nome;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;

    @OneToMany(mappedBy = "curso")
    private List<Semestre> semestres;
}

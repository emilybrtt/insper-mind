package br.insper.insperMind.disciplina;

import br.insper.insperMind.comentario.Comentario;
import br.insper.insperMind.docente.Docente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private LocalDateTime dataAtualizacao;

    @OneToMany
    private List<Comentario> relatos;

    @ManyToOne
    private Docente docente;

    private String formulaAvaliacao;

    private Boolean temDelta;

    private String criterioBarreira;
}
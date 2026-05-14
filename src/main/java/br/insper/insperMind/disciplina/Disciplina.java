package br.insper.insperMind.disciplina;

import br.insper.insperMind.comentario.Comentario;
import br.insper.insperMind.docente.Docente;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.semestre.Semestre;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @OneToMany(mappedBy = "disciplina")
    private List<Comentario> relatos;

    @OneToMany(mappedBy = "disciplina")
    private List<Material> materiais;

    @ManyToMany
    @JoinTable(
            name = "disciplina_docente",
            joinColumns = @JoinColumn(name = "id_disciplina"),
            inverseJoinColumns = @JoinColumn(name = "id_docente")
    )
    private List<Docente> docentes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "id_semestre")
    private Semestre semestre;

    @Column(nullable = false)
    private String formulaAvaliacao;

    @Column(nullable = false)
    private Boolean temDelta;

    @Column(nullable = false)
    private String criterioBarreira;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;
}
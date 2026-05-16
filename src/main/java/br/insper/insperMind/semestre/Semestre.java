package br.insper.insperMind.semestre;

import br.insper.insperMind.curso.Curso;
import br.insper.insperMind.disciplina.Disciplina;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"nome", "id_curso"}))
public class Semestre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_curso", nullable = false)
    private Curso curso;

    @OneToMany(mappedBy = "semestre")
    private List<Disciplina> disciplinas;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}

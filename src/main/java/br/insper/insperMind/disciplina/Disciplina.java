package br.insper.insperMind.disciplina;

import br.insper.insperMind.comentario.Comentario;
import br.insper.insperMind.docente.Docente;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
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

    @ManyToOne
    @JoinColumn(name = "id_docente")
    private Docente docente;

    @Column(nullable = false)
    private String formulaAvaliacao;

    @Column(nullable = false)
    private Boolean temDelta;

    @Column(nullable = false)
    private String criterioBarreira;
}
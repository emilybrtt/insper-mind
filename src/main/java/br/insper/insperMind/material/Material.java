package br.insper.insperMind.material;

import br.insper.insperMind.curso.Curso;
import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String titulo;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String link;

    @Enumerated(EnumType.STRING)
    private TipoMaterial tipo;

    @ManyToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name="id_curso")
    private Curso curso;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;
}
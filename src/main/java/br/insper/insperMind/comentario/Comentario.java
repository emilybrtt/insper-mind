package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String comentario;

    @Column(nullable = false)
    private Integer curtidas = 0;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @ManyToOne
    @JoinColumn(name = "id_material")
    private Material material;

    @ManyToOne
    @JoinColumn(name = "id_comentario_pai")
    private Comentario comentarioPai;

    @OneToMany(mappedBy = "comentarioPai")
    private List<Comentario> respostas;

    @ManyToMany
    @JoinTable(
            name = "comentario_curtida",
            joinColumns = @JoinColumn(name = "comentario_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuariosQueCurtiram = new ArrayList<>();

}
package br.insper.insperMind.material;

import br.insper.insperMind.curso.Curso;
import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
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

    public static Material toModel(SaveMaterialDTO dto, Usuario usuario, Curso curso) {
        Material material = new Material();
        material.setTitulo(dto.getTitulo());
        material.setDescricao(dto.getDescricao());
        material.setLink(dto.getLink());
        material.setTipo(TipoMaterial.valueOf(dto.getTipo()));
        material.setUsuario(usuario);
        material.setCurso(curso);
        material.setAtivo(true);
        return material;
    }

    public void update(EditMaterialDTO dto, Curso curso) {
        if (dto.getTitulo() != null) this.titulo = dto.getTitulo();
        if (dto.getDescricao() != null) this.descricao = dto.getDescricao();
        if (dto.getLink() != null) this.link = dto.getLink();
        if (dto.getTipo() != null) this.tipo = TipoMaterial.valueOf(dto.getTipo());
        if (dto.getAtivo() != null) this.ativo = dto.getAtivo();
        if (curso != null) this.curso = curso;
    }
}
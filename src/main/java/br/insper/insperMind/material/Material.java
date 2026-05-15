package br.insper.insperMind.material;

import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
import br.insper.insperMind.material.exception.MaterialInvalidTypeException;
import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_disciplina")
    private Disciplina disciplina;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @NotNull
    @Column(nullable = false)
    private Boolean ativo = true;

    @Column(nullable = false)
    private Integer curtidas = 0;

    @Column(nullable = true)
    private String arquivo;

    @ManyToMany
    @JoinTable(
            name = "material_curtida",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuariosQueCurtiram = new ArrayList<>();

    public static Material toModel(SaveMaterialDTO dto, Usuario usuario, Disciplina disciplina) {
        Material material = new Material();
        material.setTitulo(dto.getTitulo());
        material.setDescricao(dto.getDescricao());
        material.setLink(dto.getLink());

        try {
            material.setTipo(TipoMaterial.valueOf(dto.getTipo().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new MaterialInvalidTypeException();
        }

        material.setUsuario(usuario);
        material.setDisciplina(disciplina);
        material.setAtivo(true);
        return material;
    }

    public void update(EditMaterialDTO dto, Disciplina disciplina) {
        if (dto.getTitulo() != null) {
            this.titulo = dto.getTitulo();
        }
        if (dto.getDescricao() != null) {
            this.descricao = dto.getDescricao();
        }
        if (dto.getLink() != null) {
            this.link = dto.getLink();
        }
        if (dto.getTipo() != null) {
            try {
                this.tipo = TipoMaterial.valueOf(dto.getTipo().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new MaterialInvalidTypeException();
            }
        }
        if (dto.getAtivo() != null) {
            this.ativo = dto.getAtivo();
        }
        if (disciplina != null) {
            this.disciplina = disciplina;
        }
    }

    public static Material criarDoArquivo(String nomeArquivo, Usuario usuario, Disciplina disciplina) {
        Material material = new Material();
        material.setTitulo(nomeArquivo);
        material.setDescricao("");
        material.setLink("");
        material.setArquivo(nomeArquivo);
        material.setTipo(TipoMaterial.OUTRO);
        material.setUsuario(usuario);
        material.setDisciplina(disciplina);
        material.setAtivo(true);
        return material;
    }
}
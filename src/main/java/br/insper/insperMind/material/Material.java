package br.insper.insperMind.material;

import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
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

    private String titulo;

    private String descricao;

    private String link;

    @Enumerated(EnumType.STRING)
    private TipoMaterial tipo;

    @ManyToOne
    private Usuario usuario;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private Boolean ativo = true;
}
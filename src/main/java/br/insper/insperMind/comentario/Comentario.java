package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public static Comentario toModel(SaveComentarioDTO dto, Usuario usuario) {
        Comentario comentario = new Comentario();
        comentario.setComentario(dto.getComentario());
        comentario.setCurtidas(0);
        comentario.setUsuario(usuario);
        comentario.setAtivo(true);
        return comentario;
    }
}
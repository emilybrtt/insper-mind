package br.insper.insperMind.favorito;

import br.insper.insperMind.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Favorito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Usuario usuario;

    private Integer itemId;

    private String tipoItem;

    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime dataSalvo;
}
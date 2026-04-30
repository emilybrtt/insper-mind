package br.insper.insperMind.favorito;

import br.insper.insperMind.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {

    boolean existsByUsuarioAndMaterial_IdAndAtivoTrue(
            Usuario usuario,
            Integer materialId
    );

    boolean existsByUsuarioAndEletiva_IdAndAtivoTrue(
            Usuario usuario,
            Integer eletivaId
    );

    Page<Favorito> findByAtivoTrue(Pageable pageable);
}
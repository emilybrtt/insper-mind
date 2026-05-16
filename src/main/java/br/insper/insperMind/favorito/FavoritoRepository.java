package br.insper.insperMind.favorito;

import br.insper.insperMind.eletiva.Eletiva;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoritoRepository extends JpaRepository<Favorito, Integer> {
    boolean existsByUsuarioAndMaterial(Usuario usuario, Material material);
    boolean existsByUsuarioAndEletiva(Usuario usuario, Eletiva eletiva);
    Page<Favorito> findByUsuarioAndAtivoTrue(Usuario usuario, Pageable pageable);
    Page<Favorito> findByUsuarioAndAtivoTrueAndMaterialIsNotNull(Usuario usuario, Pageable pageable);
    Page<Favorito> findByUsuarioAndAtivoTrueAndEletivaIsNotNull(Usuario usuario, Pageable pageable);
}
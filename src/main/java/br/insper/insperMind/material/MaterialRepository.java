package br.insper.insperMind.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByTituloAndAtivoTrue(String titulo);

    Page<Material> findByAtivoTrue(Pageable pageable);

    Page<Material> findByAtivoTrueAndDisciplinaId(Integer disciplinaId, Pageable pageable);

    Page<Material> findByAtivoTrueAndUsuarioEmail(String email, Pageable pageable);

    Page<Material> findByAtivoTrueAndTipo(TipoMaterial tipo, Pageable pageable);
}
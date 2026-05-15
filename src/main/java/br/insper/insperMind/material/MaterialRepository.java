package br.insper.insperMind.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByTituloAndAtivoTrue(String titulo);
    Page<Material> findByAtivoTrue(Pageable pageable);
    Page<Material> findByDisciplinaSemestreCursoIdAndTipo(Integer cursoId, String tipo, Pageable pageable);
    Page<Material> findByDisciplinaIdAndTipo(Integer disciplinaId, String tipo, Pageable pageable);
    Page<Material> findByTipo(String tipo, Pageable pageable);
}
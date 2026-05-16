package br.insper.insperMind.material;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByTituloAndAtivoTrue(String titulo);
    Page<Material> findByAtivoTrue(Pageable pageable);
    Page<Material> findByUsuarioEmailAndAtivoTrue(String email, Pageable pageable);
    @Query("SELECT m FROM Material m WHERE m.disciplina.semestre.curso.id = :cursoId AND (:tipo IS NULL OR m.tipo = :tipo) AND m.ativo = true")
    Page<Material> findByDisciplinaSemestreCursoIdAndTipo(@Param("cursoId") Integer cursoId, @Param("tipo") TipoMaterial tipo, Pageable pageable);

    @Query("SELECT m FROM Material m WHERE m.disciplina.id = :disciplinaId AND (:tipo IS NULL OR m.tipo = :tipo) AND m.ativo = true")
    Page<Material> findByDisciplinaIdAndTipo(@Param("disciplinaId") Integer disciplinaId, @Param("tipo") TipoMaterial tipo, Pageable pageable);
    Page<Material> findByTipo(TipoMaterial tipo, Pageable pageable);
}
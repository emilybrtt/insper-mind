package br.insper.insperMind.comentario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Integer> {

    Page<Comentario> findByAtivoTrue(Pageable pageable);

    Page<Comentario> findByAtivoTrueAndDisciplinaId(Integer idDisciplina, Pageable pageable);

    Page<Comentario> findByAtivoTrueAndMaterialId(Integer idMaterial, Pageable pageable);

    Page<Comentario> findByAtivoTrueAndComentarioPaiIsNull(Pageable pageable);

    Page<Comentario> findByAtivoTrueAndComentarioPaiId(Integer comentarioPaiId, Pageable pageable);

    List<Comentario> findByComentarioPaiIdAndAtivoTrue(Integer comentarioPaiId);
}
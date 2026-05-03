package br.insper.insperMind.disciplina;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import org.springframework.data.domain.Page;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    Page<Disciplina> findByAtivoTrue(Pageable pageable);
}
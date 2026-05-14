package br.insper.insperMind.disciplina;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;

@Repository
public interface DisciplinaRepository extends JpaRepository<Disciplina, Integer> {
    boolean existsByNomeAndAtivoTrue(String nome);
    Page<Disciplina> findByAtivoTrue(Pageable pageable);

}
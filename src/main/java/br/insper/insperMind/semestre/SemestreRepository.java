package br.insper.insperMind.semestre;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemestreRepository extends JpaRepository<Semestre, Integer> {

    Page<Semestre> findByAtivoTrue(Pageable pageable);

}
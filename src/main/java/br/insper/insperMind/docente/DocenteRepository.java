package br.insper.insperMind.docente;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    Optional<Docente> findByEmail(String email);
    boolean existsByNome(String nome);
    boolean existsByEmail(String email);
    Page<Docente> findByAtivoTrue(Pageable pageable);
}
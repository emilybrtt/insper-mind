package br.insper.insperMind.docente;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.nio.channels.FileChannel;
import java.util.Optional;
import org.springframework.data.domain.Page;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Integer> {
    Optional<Docente> findByEmail(String email);

    Page<Docente> findByAtivoTrue(Pageable pageable);
}
package br.insper.insperMind.eletiva;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EletivaRepository extends JpaRepository<Eletiva, Integer> {
    Page<Eletiva> findByAtivoTrue(Pageable pageable);
}

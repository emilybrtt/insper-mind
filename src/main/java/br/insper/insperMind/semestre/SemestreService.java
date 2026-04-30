package br.insper.insperMind.semestre;

import br.insper.insperMind.semestre.dto.EditSemestreDTO;
import br.insper.insperMind.semestre.dto.ResponseSemestreDTO;
import br.insper.insperMind.semestre.dto.SaveSemestreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SemestreService {

    @Autowired
    private SemestreRepository semestreRepository;

    public ResponseSemestreDTO save(SaveSemestreDTO dto) {
        Semestre semestre = new Semestre();

        semestre.setNome(dto.getNome());
        semestre.setAtivo(true);

        semestre = semestreRepository.save(semestre);

        return ResponseSemestreDTO.toDTO(semestre);
    }

    public Page<ResponseSemestreDTO> list(Pageable pageable) {
        return semestreRepository.findByAtivoTrue(pageable)
                .map(ResponseSemestreDTO::toDTO);
    }

    public ResponseSemestreDTO findById(Integer id) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre não encontrado"));

        return ResponseSemestreDTO.toDTO(semestre);
    }

    public Semestre findModelById(Integer id) {
        return semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre não encontrado"));
    }

    public ResponseSemestreDTO edit(Integer id, EditSemestreDTO dto) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre não encontrado"));

        if (dto.getNome() != null) {
            semestre.setNome(dto.getNome());
        }

        if (dto.getAtivo() != null) {
            semestre.setAtivo(dto.getAtivo());
        }

        semestre = semestreRepository.save(semestre);

        return ResponseSemestreDTO.toDTO(semestre);
    }

    public void delete(Integer id) {
        Semestre semestre = semestreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Semestre não encontrado"));

        semestre.setAtivo(false);
        semestreRepository.save(semestre);
    }
}
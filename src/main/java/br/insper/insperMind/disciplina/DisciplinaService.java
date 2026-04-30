package br.insper.insperMind.disciplina;

import br.insper.insperMind.disciplina.dto.ResponseDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.SaveDisciplinaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public ResponseDisciplinaDTO save(SaveDisciplinaDTO dto) {
        Disciplina disciplina = new Disciplina();

        disciplina.setNome(dto.getNome());
        disciplina.setFormulaAvaliacao(dto.getFormulaAvaliacao());
        disciplina.setTemDelta(dto.getTemDelta());
        disciplina.setCriterioBarreira(dto.getCriterioBarreira());
        disciplina.setDataAtualizacao(LocalDateTime.now());

        disciplina = disciplinaRepository.save(disciplina);

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }

    public Page<ResponseDisciplinaDTO> list(Pageable pageable) {
        return disciplinaRepository.findAll(pageable)
                .map(ResponseDisciplinaDTO::toDTO);
    }

    public ResponseDisciplinaDTO findById(Integer id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }

    public void delete(Integer id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Disciplina não encontrada"));

        disciplinaRepository.delete(disciplina);
    }
}

package br.insper.insperMind.disciplina;

import br.insper.insperMind.disciplina.dto.EditDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.ResponseDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.SaveDisciplinaDTO;
import br.insper.insperMind.disciplina.exception.DisciplinaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    public Disciplina get(Integer id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException("Disciplina não encontrada"));
        if (!disciplina.getAtivo()) {
            throw new DisciplinaNotFoundException("Disciplina não encontrada");
        }
        return disciplina;
    }


    public ResponseDisciplinaDTO getDTO(Integer id) {
        return ResponseDisciplinaDTO.toDTO(get(id));
    }


    public ResponseDisciplinaDTO save(SaveDisciplinaDTO dto) {
        Disciplina disciplina = new Disciplina();

        disciplina.setNome(dto.getNome());
        disciplina.setFormulaAvaliacao(dto.getFormulaAvaliacao());
        disciplina.setTemDelta(dto.getTemDelta());
        disciplina.setCriterioBarreira(dto.getCriterioBarreira());
        disciplina.setAtivo(true);
        disciplina.setDataAtualizacao(LocalDateTime.now());

        disciplina = disciplinaRepository.save(disciplina);

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }


    public Page<ResponseDisciplinaDTO> list(Pageable pageable) {
        return disciplinaRepository.findByAtivoTrue(pageable)
                .map(ResponseDisciplinaDTO::toDTO);
    }


    public ResponseDisciplinaDTO edit(Integer id, EditDisciplinaDTO dto) {
        Disciplina disciplina = get(id);

        if (dto.getNome() != null) {
            disciplina.setNome(dto.getNome());
        }

        if (dto.getFormulaAvaliacao() != null) {
            disciplina.setFormulaAvaliacao(dto.getFormulaAvaliacao());
        }

        if (dto.getTemDelta() != null) {
            disciplina.setTemDelta(dto.getTemDelta());
        }

        if (dto.getCriterioBarreira() != null) {
            disciplina.setCriterioBarreira(dto.getCriterioBarreira());
        }

        disciplina.setDataAtualizacao(LocalDateTime.now());

        disciplina = disciplinaRepository.save(disciplina);

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }


    public void delete(Integer id) {
        Disciplina disciplina = get(id);
        disciplina.setAtivo(false);
        disciplinaRepository.save(disciplina);
    }
}
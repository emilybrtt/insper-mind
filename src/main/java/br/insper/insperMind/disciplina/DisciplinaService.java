package br.insper.insperMind.disciplina;

import br.insper.insperMind.disciplina.dto.EditDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.ResponseDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.SaveDisciplinaDTO;
import br.insper.insperMind.disciplina.exception.DisciplinaAlreadyExistsException;
import br.insper.insperMind.disciplina.exception.DisciplinaNotFoundException;
import br.insper.insperMind.semestre.Semestre;
import br.insper.insperMind.semestre.SemestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private SemestreService semestreService;

    public Disciplina get(Integer id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(() -> new DisciplinaNotFoundException());
        if (!disciplina.getAtivo()) {
            throw new DisciplinaNotFoundException();
        }
        return disciplina;
    }


    public ResponseDisciplinaDTO getDTO(Integer id) {
        return ResponseDisciplinaDTO.toDTO(get(id));
    }


    public ResponseDisciplinaDTO save(SaveDisciplinaDTO dto) {
        if (disciplinaRepository.existsByNome(dto.getNome())) {
            throw new DisciplinaAlreadyExistsException();
        }
        Semestre semestre = semestreService.get(dto.getSemestreId());

        Disciplina disciplina = new Disciplina();

        disciplina.setNome(dto.getNome());
        disciplina.setSemestre(semestre);
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

        if (dto.getSemestreId() != null) {
            Semestre semestre = semestreService.get(dto.getSemestreId());
            disciplina.setSemestre(semestre);
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

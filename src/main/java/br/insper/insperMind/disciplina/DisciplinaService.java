package br.insper.insperMind.disciplina;

import br.insper.insperMind.disciplina.dto.EditDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.ResponseDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.SaveDisciplinaDTO;
import br.insper.insperMind.disciplina.exception.DisciplinaAlreadyExistsException;
import br.insper.insperMind.disciplina.exception.DisciplinaDocenteNotFoundException;
import br.insper.insperMind.disciplina.exception.DisciplinaNotFoundException;
import br.insper.insperMind.disciplina.exception.DisciplinaSemDocentesException;
import br.insper.insperMind.docente.Docente;
import br.insper.insperMind.docente.DocenteService;
import br.insper.insperMind.semestre.Semestre;
import br.insper.insperMind.semestre.SemestreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    private DisciplinaRepository disciplinaRepository;

    @Autowired
    private SemestreService semestreService;

    @Autowired
    private DocenteService docenteService;

    public Disciplina get(Integer id) {
        Disciplina disciplina = disciplinaRepository.findById(id)
                .orElseThrow(DisciplinaNotFoundException::new);

        if (!disciplina.getAtivo()) {
            throw new DisciplinaNotFoundException();
        }

        return disciplina;
    }

    public ResponseDisciplinaDTO getDTO(Integer id) {
        return ResponseDisciplinaDTO.toDTO(get(id));
    }

    public ResponseDisciplinaDTO save(SaveDisciplinaDTO dto) {
        if (disciplinaRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
            throw new DisciplinaAlreadyExistsException();
        }

        Semestre semestre = semestreService.get(dto.getSemestreId());
        List<Docente> docentes = carregarDocentes(dto.getDocenteIds());

        Disciplina disciplina = new Disciplina();
        disciplina.setNome(dto.getNome());
        disciplina.setSemestre(semestre);
        disciplina.setDocentes(docentes);
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
            if (!dto.getNome().equals(disciplina.getNome())
                    && disciplinaRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
                throw new DisciplinaAlreadyExistsException();
            }
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

    public ResponseDisciplinaDTO addDocente(Integer disciplinaId, Integer docenteId) {
        Disciplina disciplina = get(disciplinaId);
        Docente docente = docenteService.get(docenteId);

        boolean jaExiste = disciplina.getDocentes().stream()
                .anyMatch(d -> d.getId().equals(docente.getId()));

        if (!jaExiste) {
            disciplina.getDocentes().add(docente);
            disciplina = disciplinaRepository.save(disciplina);
        }

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }

    public ResponseDisciplinaDTO removeDocente(Integer disciplinaId, Integer docenteId) {
        Disciplina disciplina = get(disciplinaId);
        Docente docente = docenteService.get(docenteId);

        if (disciplina.getDocentes() != null) {
            disciplina.getDocentes().removeIf(d -> d.getId().equals(docente.getId()));
            disciplina = disciplinaRepository.save(disciplina);
        }

        return ResponseDisciplinaDTO.toDTO(disciplina);
    }

    public void delete(Integer id) {
        Disciplina disciplina = get(id);
        disciplina.setAtivo(false);
        disciplinaRepository.save(disciplina);
    }

    private List<Docente> carregarDocentes(List<Integer> docenteIds) {
        if (docenteIds == null || docenteIds.isEmpty()) {
            throw new DisciplinaSemDocentesException();
        }

        List<Docente> docentes = docenteService.findAllById(docenteIds);

        if (docentes.size() != docenteIds.size()) {
            throw new DisciplinaDocenteNotFoundException();
        }

        return docentes;
    }
}
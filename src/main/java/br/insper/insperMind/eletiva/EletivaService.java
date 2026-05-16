package br.insper.insperMind.eletiva;

import br.insper.insperMind.docente.Docente;
import br.insper.insperMind.docente.DocenteService;
import br.insper.insperMind.eletiva.dto.EditEletivaDTO;
import br.insper.insperMind.eletiva.dto.ResponseEletivaDTO;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import br.insper.insperMind.eletiva.exception.EletivaAlreadyExistsException;
import br.insper.insperMind.eletiva.exception.EletivaDocenteNotFoundException;
import br.insper.insperMind.eletiva.exception.EletivaNotFoundException;
import br.insper.insperMind.eletiva.exception.EletivaSemDocentesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EletivaService {

    @Autowired
    private EletivaRepository eletivaRepository;

    @Autowired
    private DocenteService docenteService;

    public Eletiva get(Integer id) {
        Eletiva eletiva = eletivaRepository.findById(id)
                .orElseThrow(EletivaNotFoundException::new);

        if (!eletiva.getAtivo()) {
            throw new EletivaNotFoundException();
        }

        return eletiva;
    }

    public ResponseEletivaDTO getDTO(Integer id) {
        return ResponseEletivaDTO.toDTO(get(id));
    }

    public ResponseEletivaDTO save(SaveEletivaDTO dto) {
        if (eletivaRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
            throw new EletivaAlreadyExistsException();
        }

        List<Docente> docentes = carregarDocentes(dto.getDocenteIds());

        Eletiva eletiva = Eletiva.toModel(dto);
        eletiva.setDocentes(docentes);

        eletiva = eletivaRepository.save(eletiva);
        return ResponseEletivaDTO.toDTO(eletiva);
    }

    public Page<ResponseEletivaDTO> list(Pageable pageable) {
        return eletivaRepository.findByAtivoTrue(pageable)
                .map(ResponseEletivaDTO::toDTO);
    }

    public ResponseEletivaDTO edit(Integer id, EditEletivaDTO dto) {
        Eletiva eletiva = get(id);

        if (dto.getNome() != null) {
            if (!dto.getNome().equals(eletiva.getNome())
                    && eletivaRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
                throw new EletivaAlreadyExistsException();
            }
            eletiva.setNome(dto.getNome());
        }

        if (dto.getCargaHoraria() != null) {
            eletiva.setCargaHoraria(dto.getCargaHoraria());
        }

        if (dto.getSemestreMinimo() != null) {
            eletiva.setSemestreMinimo(dto.getSemestreMinimo());
        }

        if (dto.getFormulaAvaliacao() != null) {
            eletiva.setFormulaAvaliacao(dto.getFormulaAvaliacao());
        }

        if (dto.getTemDelta() != null) {
            eletiva.setTemDelta(dto.getTemDelta());
        }

        if (dto.getCriterioBarreira() != null) {
            eletiva.setCriterioBarreira(dto.getCriterioBarreira());
        }

        eletiva = eletivaRepository.save(eletiva);
        return ResponseEletivaDTO.toDTO(eletiva);
    }

    public ResponseEletivaDTO addDocente(Integer eletivaId, Integer docenteId) {
        Eletiva eletiva = get(eletivaId);
        Docente docente = docenteService.get(docenteId);

        boolean jaExiste = eletiva.getDocentes().stream()
                .anyMatch(d -> d.getId().equals(docente.getId()));

        if (!jaExiste) {
            eletiva.getDocentes().add(docente);
            eletiva = eletivaRepository.save(eletiva);
        }

        return ResponseEletivaDTO.toDTO(eletiva);
    }

    public ResponseEletivaDTO removeDocente(Integer eletivaId, Integer docenteId) {
        Eletiva eletiva = get(eletivaId);
        Docente docente = docenteService.get(docenteId);

        eletiva.getDocentes().removeIf(d -> d.getId().equals(docente.getId()));
        eletiva = eletivaRepository.save(eletiva);

        return ResponseEletivaDTO.toDTO(eletiva);
    }

    public void delete(Integer id) {
        Eletiva eletiva = get(id);
        eletiva.setAtivo(false);
        eletivaRepository.save(eletiva);
    }

    private List<Docente> carregarDocentes(List<Integer> docenteIds) {
        if (docenteIds == null || docenteIds.isEmpty()) {
            throw new EletivaSemDocentesException();
        }

        List<Docente> docentes = docenteService.findAllById(docenteIds);

        if (docentes.size() != docenteIds.size()) {
            throw new EletivaDocenteNotFoundException();
        }

        return docentes;
    }
}
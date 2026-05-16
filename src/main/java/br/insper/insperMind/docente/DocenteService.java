package br.insper.insperMind.docente;

import br.insper.insperMind.docente.dto.EditDocenteDTO;
import br.insper.insperMind.docente.dto.ResponseDocenteDTO;
import br.insper.insperMind.docente.dto.SaveDocenteDTO;
import br.insper.insperMind.docente.exception.DocenteAlreadyExistsException;
import br.insper.insperMind.docente.exception.DocenteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public Docente get(Integer id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(DocenteNotFoundException::new);

        if (!docente.getAtivo()) {
            throw new DocenteNotFoundException();
        }

        return docente;
    }

    public ResponseDocenteDTO getDto(Integer id) {
        return ResponseDocenteDTO.toDTO(get(id));
    }

    public ResponseDocenteDTO save(SaveDocenteDTO dto) {
        if (docenteRepository.existsByNome(dto.getNome()) || docenteRepository.existsByEmail(dto.getEmail())) {
            throw new DocenteAlreadyExistsException();
        }

        Docente docente = Docente.toModel(dto);
        docente = docenteRepository.save(docente);

        return ResponseDocenteDTO.toDTO(docente);
    }

    public Page<ResponseDocenteDTO> list(Pageable pageable) {
        return docenteRepository.findByAtivoTrue(pageable)
                .map(ResponseDocenteDTO::toDTO);
    }

    public ResponseDocenteDTO update(Integer id, EditDocenteDTO dto) {
        Docente docente = get(id);

        if (dto.getNome() != null
                && !dto.getNome().equals(docente.getNome())
                && docenteRepository.existsByNome(dto.getNome())) {
            throw new DocenteAlreadyExistsException();
        }

        if (dto.getEmail() != null
                && !dto.getEmail().equals(docente.getEmail())
                && docenteRepository.existsByEmail(dto.getEmail())) {
            throw new DocenteAlreadyExistsException();
        }

        if (dto.getNome() != null) {
            docente.setNome(dto.getNome());
        }

        if (dto.getEmail() != null) {
            docente.setEmail(dto.getEmail());
        }

        docente = docenteRepository.save(docente);
        return ResponseDocenteDTO.toDTO(docente);
    }

    public void delete(Integer id) {
        Docente docente = get(id);
        docente.setAtivo(false);
        docenteRepository.save(docente);
    }

    public List<Docente> findAllById(List<Integer> ids) {
        return docenteRepository.findAllById(ids);
    }
}
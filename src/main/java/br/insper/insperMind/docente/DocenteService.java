package br.insper.insperMind.docente;

import br.insper.insperMind.docente.Docente;
import br.insper.insperMind.docente.dto.EditDocenteDTO;
import br.insper.insperMind.docente.dto.ResponseDocenteDTO;
import br.insper.insperMind.docente.dto.SaveDocenteDTO;
import br.insper.insperMind.docente.exception.DocenteNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DocenteService {

    @Autowired
    private DocenteRepository docenteRepository;

    public ResponseDocenteDTO save(SaveDocenteDTO dto) {
        Docente docente = Docente.toModel(dto);
        docente = docenteRepository.save(docente);
        return ResponseDocenteDTO.toDTO(docente);
    }

    public Page<ResponseDocenteDTO> list(Pageable pageable) {
        return docenteRepository.findAll(pageable)
                .map(ResponseDocenteDTO::toDTO);
    }

    public Docente findByEmail(String email) {
        return docenteRepository.findByEmail(email)
                .orElseThrow(() -> new DocenteNotFoundException("Docente não encontrado"));
    }

    public ResponseDocenteDTO getDto(String email) {
        return ResponseDocenteDTO.toDTO(findByEmail(email));
    }

    public ResponseDocenteDTO update(String email, EditDocenteDTO dto) {
        Docente docente = findByEmail(email);
        if (dto.getNome() != null) docente.setNome(dto.getNome());
        return ResponseDocenteDTO.toDTO(docenteRepository.save(docente));
    }

    public void delete(Integer id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new DocenteNotFoundException("Docente não encontrado"));

        docente.setAtivo(false);
        docenteRepository.save(docente);
    }

}
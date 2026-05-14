package br.insper.insperMind.curso;

import br.insper.insperMind.curso.dto.EditCursoDTO;
import br.insper.insperMind.curso.dto.ResponseCursoDTO;
import br.insper.insperMind.curso.dto.SaveCursoDTO;
import br.insper.insperMind.curso.exception.CursoAlreadyExistsException;
import br.insper.insperMind.curso.exception.CursoNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    public Curso get(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(CursoNotFoundException::new);

        if (!curso.getAtivo()) {
            throw new CursoNotFoundException();
        }

        return curso;
    }

    public ResponseCursoDTO getDTO(Integer id) {
        return ResponseCursoDTO.toDTO(get(id));
    }

    public ResponseCursoDTO save(SaveCursoDTO dto) {
        if (cursoRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
            throw new CursoAlreadyExistsException();
        }

        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setAtivo(true);

        curso = cursoRepository.save(curso);

        return ResponseCursoDTO.toDTO(curso);
    }

    public Page<ResponseCursoDTO> list(Pageable pageable) {
        return cursoRepository.findByAtivoTrue(pageable)
                .map(ResponseCursoDTO::toDTO);
    }

    public ResponseCursoDTO edit(Integer id, EditCursoDTO dto) {
        Curso curso = get(id);

        if (dto.getNome() != null) {
            if (cursoRepository.existsByNomeAndAtivoTrue(dto.getNome())) {
                throw new CursoAlreadyExistsException();
            }
            curso.setNome(dto.getNome());
        }

        if (dto.getAtivo() != null) {
            curso.setAtivo(dto.getAtivo());
        }

        curso = cursoRepository.save(curso);
        return ResponseCursoDTO.toDTO(curso);
    }

    public void delete(Integer id) {
        Curso curso = get(id);

        curso.setAtivo(false);
        cursoRepository.save(curso);
    }
}
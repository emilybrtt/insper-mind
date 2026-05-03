package br.insper.insperMind.curso;

import br.insper.insperMind.curso.dto.EditCursoDTO;
import br.insper.insperMind.curso.dto.ResponseCursoDTO;
import br.insper.insperMind.curso.dto.SaveCursoDTO;
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
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        if (!curso.getAtivo()) {
            throw new RuntimeException("Curso não encontrado");
        }

        return curso;
    }

    public ResponseCursoDTO getDTO(Integer id) {
        return ResponseCursoDTO.toDTO(get(id));
    }

    public ResponseCursoDTO save(SaveCursoDTO dto) {
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
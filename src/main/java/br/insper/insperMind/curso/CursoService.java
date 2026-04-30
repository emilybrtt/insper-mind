package br.insper.insperMind.curso;

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

    public ResponseCursoDTO findById(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        return ResponseCursoDTO.toDTO(curso);
    }

    public Curso findModelById(Integer id) {
        return cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
    }

    public void delete(Integer id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        curso.setAtivo(false);
        cursoRepository.save(curso);
    }
}
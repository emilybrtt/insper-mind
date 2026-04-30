package br.insper.insperMind.curso;

import br.insper.insperMind.curso.dto.ResponseCursoDTO;
import br.insper.insperMind.curso.dto.SaveCursoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    public ResponseCursoDTO saveCurso(@RequestBody SaveCursoDTO dto) {
        return cursoService.save(dto);
    }

    @GetMapping
    public Page<ResponseCursoDTO> listCursos(Pageable pageable) {
        return cursoService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseCursoDTO getCurso(@PathVariable Integer id) {
        return cursoService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCurso(@PathVariable Integer id) {
        cursoService.delete(id);
    }
}
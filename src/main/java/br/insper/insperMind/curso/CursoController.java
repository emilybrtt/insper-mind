package br.insper.insperMind.curso;

import br.insper.insperMind.curso.dto.EditCursoDTO;
import br.insper.insperMind.curso.dto.ResponseCursoDTO;
import br.insper.insperMind.curso.dto.SaveCursoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/curso")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseCursoDTO saveCurso(@Valid @RequestBody SaveCursoDTO dto) {
        return cursoService.save(dto);
    }

    @GetMapping
    public Page<ResponseCursoDTO> listCursos(Pageable pageable) {
        return cursoService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseCursoDTO getCurso(@PathVariable Integer id) {
        return cursoService.getDTO(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseCursoDTO editCurso(@PathVariable Integer id, @Valid @RequestBody EditCursoDTO dto) {
        return cursoService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCurso(@PathVariable Integer id) {
        cursoService.delete(id);
    }
}
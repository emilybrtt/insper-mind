package br.insper.insperMind.disciplina;

import br.insper.insperMind.disciplina.dto.EditDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.ResponseDisciplinaDTO;
import br.insper.insperMind.disciplina.dto.SaveDisciplinaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    private DisciplinaService disciplinaService;

    @PostMapping
    public ResponseDisciplinaDTO saveDisciplina(@Valid @RequestBody SaveDisciplinaDTO dto) {
        return disciplinaService.save(dto);
    }

    @GetMapping
    public Page<ResponseDisciplinaDTO> listDisciplinas(Pageable pageable) {
        return disciplinaService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseDisciplinaDTO getDisciplina(@PathVariable Integer id) {
        return disciplinaService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseDisciplinaDTO editDisciplina(@PathVariable Integer id,
                                                @Valid @RequestBody EditDisciplinaDTO dto) {
        return disciplinaService.edit(id, dto);
    }

    @PostMapping("/{id}/docentes/{docenteId}")
    public ResponseDisciplinaDTO addDocente(@PathVariable Integer id,
                                            @PathVariable Integer docenteId) {
        return disciplinaService.addDocente(id, docenteId);
    }

    @DeleteMapping("/{id}/docentes/{docenteId}")
    public ResponseDisciplinaDTO removeDocente(@PathVariable Integer id,
                                               @PathVariable Integer docenteId) {
        return disciplinaService.removeDocente(id, docenteId);
    }

    @DeleteMapping("/{id}")
    public void deleteDisciplina(@PathVariable Integer id) {
        disciplinaService.delete(id);
    }
}
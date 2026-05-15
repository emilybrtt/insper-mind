package br.insper.insperMind.docente;

import br.insper.insperMind.docente.dto.EditDocenteDTO;
import br.insper.insperMind.docente.dto.ResponseDocenteDTO;
import br.insper.insperMind.docente.dto.SaveDocenteDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/docente")
public class DocenteController {

    @Autowired
    private DocenteService docenteService;

    @GetMapping
    public Page<ResponseDocenteDTO> listDocentes(Pageable pageable) {
        return docenteService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseDocenteDTO getDocente(@PathVariable Integer id) {
        return docenteService.getDto(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDocenteDTO saveDocente(@Valid @RequestBody SaveDocenteDTO dto) {
        return docenteService.save(dto);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseDocenteDTO updateDocente(@PathVariable Integer id,
                                            @Valid @RequestBody EditDocenteDTO dto) {
        return docenteService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteDocente(@PathVariable Integer id) {
        docenteService.delete(id);
    }
}
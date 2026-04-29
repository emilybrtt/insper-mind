package br.insper.insperMind.docente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/{email}")
    public ResponseDocenteDTO getDocente(@PathVariable String email) {
        return docenteService.getDto(email);
    }

    @PostMapping
    public ResponseDocenteDTO saveDocente(@RequestBody SaveDocenteDTO dto) {
        return docenteService.save(dto);
    }

    @PatchMapping("/{email}")
    public ResponseDocenteDTO updateDocente(@PathVariable String email, @RequestBody EditDocenteDTO dto) {
        return docenteService.update(email, dto);
    }
}
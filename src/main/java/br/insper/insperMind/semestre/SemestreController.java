package br.insper.insperMind.semestre;

import br.insper.insperMind.semestre.dto.EditSemestreDTO;
import br.insper.insperMind.semestre.dto.ResponseSemestreDTO;
import br.insper.insperMind.semestre.dto.SaveSemestreDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/semestre")
public class SemestreController {

    @Autowired
    private SemestreService semestreService;

    @PostMapping
    public ResponseSemestreDTO save(@RequestBody SaveSemestreDTO dto) {
        return semestreService.save(dto);
    }

    @GetMapping
    public Page<ResponseSemestreDTO> list(Pageable pageable) {
        return semestreService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseSemestreDTO getById(@PathVariable Integer id) {
        return semestreService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseSemestreDTO edit(@PathVariable Integer id, @RequestBody EditSemestreDTO dto) {
        return semestreService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        semestreService.delete(id);
    }
}
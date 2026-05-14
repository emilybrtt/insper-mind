package br.insper.insperMind.eletiva;

import br.insper.insperMind.eletiva.dto.EditEletivaDTO;
import br.insper.insperMind.eletiva.dto.ResponseEletivaDTO;
import br.insper.insperMind.eletiva.dto.SaveEletivaDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eletivas")
public class EletivaController {

    @Autowired
    private EletivaService eletivaService;

    @PostMapping
    public ResponseEletivaDTO saveEletiva(@Valid @RequestBody SaveEletivaDTO dto) {
        return eletivaService.save(dto);
    }

    @GetMapping
    public Page<ResponseEletivaDTO> listEletivas(Pageable pageable) {
        return eletivaService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEletivaDTO getEletiva(@PathVariable Integer id) {
        return eletivaService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseEletivaDTO editEletiva(@PathVariable Integer id,
                                          @Valid @RequestBody EditEletivaDTO dto) {
        return eletivaService.edit(id, dto);
    }

    @PostMapping("/{id}/docentes/{docenteId}")
    public ResponseEletivaDTO addDocente(@PathVariable Integer id,
                                         @PathVariable Integer docenteId) {
        return eletivaService.addDocente(id, docenteId);
    }

    @DeleteMapping("/{id}/docentes/{docenteId}")
    public ResponseEletivaDTO removeDocente(@PathVariable Integer id,
                                            @PathVariable Integer docenteId) {
        return eletivaService.removeDocente(id, docenteId);
    }

    @DeleteMapping("/{id}")
    public void deleteEletiva(@PathVariable Integer id) {
        eletivaService.delete(id);
    }
}
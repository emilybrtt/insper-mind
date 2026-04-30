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
    public ResponseEletivaDTO saveEletiva(@Valid @RequestBody SaveEletivaDTO saveEletivaDTO) {
        return eletivaService.save(saveEletivaDTO);
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
    public ResponseEletivaDTO editEletiva(@PathVariable Integer id, @RequestBody EditEletivaDTO editEletivaDTO) {
        return eletivaService.edit(id, editEletivaDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteEletiva(@PathVariable Integer id) {
        eletivaService.delete(id);
    }


}

package br.insper.insperMind.material;

import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    public ResponseMaterialDTO save(@Valid @RequestBody SaveMaterialDTO dto) {
        return materialService.save(dto);
    }

    @GetMapping
    public Page<ResponseMaterialDTO> list(@RequestParam(required = false) Integer disciplinaId,
                                          @RequestParam(required = false) String emailUsuario,
                                          @RequestParam(required = false) String tipo,
                                          Pageable pageable) {
        return materialService.list(disciplinaId, emailUsuario, tipo, pageable);
    }

    @GetMapping("/{id}")
    public ResponseMaterialDTO getById(@PathVariable Integer id) {
        return materialService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseMaterialDTO edit(@PathVariable Integer id,
                                    @Valid @RequestBody EditMaterialDTO dto,
                                    @RequestHeader String emailUsuario) {
        return materialService.edit(id, dto, emailUsuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id, @RequestHeader String emailUsuario) {
        materialService.delete(id, emailUsuario);
    }
}
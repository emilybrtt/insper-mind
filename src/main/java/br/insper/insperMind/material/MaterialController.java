package br.insper.insperMind.material;

import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
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
    public ResponseMaterialDTO save(@RequestBody SaveMaterialDTO dto) {
        return materialService.save(dto);
    }

    @GetMapping
    public Page<ResponseMaterialDTO> list(Pageable pageable) {
        return materialService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseMaterialDTO getById(@PathVariable Integer id) {
        return materialService.findById(id);
    }

    @PutMapping("/{id}")
    public ResponseMaterialDTO edit(@PathVariable Integer id, @RequestBody EditMaterialDTO dto) {
        return materialService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        materialService.delete(id);
    }
}
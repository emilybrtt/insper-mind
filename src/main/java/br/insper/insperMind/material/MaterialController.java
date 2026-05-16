package br.insper.insperMind.material;

import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMaterialDTO save(@Valid @RequestBody SaveMaterialDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return materialService.save(dto, email);
    }

    @GetMapping
    public Page<ResponseMaterialDTO> list(@RequestParam(required = false) Integer cursoId,
                                          @RequestParam(required = false) Integer disciplinaId,
                                          @RequestParam(required = false) String emailUsuario,
                                          @RequestParam(required = false) String tipo,
                                          Pageable pageable) {
        return materialService.list(cursoId, disciplinaId, emailUsuario, tipo, pageable);
    }

    @GetMapping("/{id}")
    public ResponseMaterialDTO getById(@PathVariable Integer id) {
        return materialService.getDTO(id);
    }

    @PutMapping("/{id}")
    public ResponseMaterialDTO edit(@PathVariable Integer id,
                                    @Valid @RequestBody EditMaterialDTO dto) {
        String emailUsuario = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return materialService.edit(id, dto, emailUsuario);
    }

    @PatchMapping("/{id}/curtir")
    public ResponseMaterialDTO curtir(@PathVariable Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return materialService.curtir(id, email);
    }

    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMaterialDTO uploadArquivo(@RequestParam MultipartFile file,
                                             @RequestParam Integer disciplinaId,
                                             @RequestParam(required=false) String titulo,
                                             @RequestParam(required=false) String descricao){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return materialService.salvarArquivo(file, disciplinaId, email, titulo, descricao);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            materialService.adminDelete(id);
        } else {
            materialService.delete(id, auth.getName());
        }
    }
}
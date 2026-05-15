package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.EditComentarioDTO;
import br.insper.insperMind.comentario.dto.ResponseComentarioDTO;
import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public Page<ResponseComentarioDTO> listComentarios(
            @RequestParam(required = false) Integer idDisciplina,
            @RequestParam(required = false) Integer idMaterial,
            @RequestParam(required = false) Integer comentarioPaiId,
            Pageable pageable) {
        return comentarioService.list(idDisciplina, idMaterial, comentarioPaiId, pageable);
    }

    @GetMapping("/{id}")
    public ResponseComentarioDTO getComentario(@PathVariable Integer id) {
        return comentarioService.getDTO(id);
    }

    @PostMapping
    public ResponseComentarioDTO saveComentario(@Valid @RequestBody SaveComentarioDTO dto) {
        return comentarioService.save(dto);
    }

    @PutMapping("/{id}")
    public ResponseComentarioDTO editComentario(@PathVariable Integer id,
                                                @RequestBody EditComentarioDTO dto) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return comentarioService.edit(id, dto, emailUsuario);
    }

    @PatchMapping("/{id}/curtir")
    public ResponseComentarioDTO curtirComentario(@PathVariable Integer id) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return comentarioService.curtir(id, emailUsuario);
    }

    @DeleteMapping("/{id}")
    public void deleteComentario(@PathVariable Integer id) {
        String emailUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        comentarioService.delete(id, emailUsuario);
    }
}
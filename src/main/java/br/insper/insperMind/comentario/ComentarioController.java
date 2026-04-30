package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.ResponseComentarioDTO;
import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comentario")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public Page<ResponseComentarioDTO> listComentarios(Pageable pageable) {
        return comentarioService.list(pageable);
    }

    @PostMapping
    public ResponseComentarioDTO saveComentario(@RequestBody SaveComentarioDTO dto) {
        return comentarioService.save(dto);
    }

    @PatchMapping("/{id}/curtir")
    public ResponseComentarioDTO curtirComentario(@PathVariable Integer id) {
        return comentarioService.curtir(id);
    }

    @DeleteMapping("/{id}")
    public void deleteComentario(@PathVariable Integer id) {
        comentarioService.delete(id);
    }
}
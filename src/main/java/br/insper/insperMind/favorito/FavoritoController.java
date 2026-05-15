package br.insper.insperMind.favorito;

import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorito")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @PostMapping
    public ResponseFavoritoDTO save(@Valid @RequestBody SaveFavoritoDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoritoService.save(dto, email);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        favoritoService.delete(id, email);
    }

    @GetMapping
    public Page<ResponseFavoritoDTO> list(@RequestParam(required = false) TipoFavorito tipo,
                                          Pageable pageable) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return favoritoService.list(email, tipo, pageable);
    }
}
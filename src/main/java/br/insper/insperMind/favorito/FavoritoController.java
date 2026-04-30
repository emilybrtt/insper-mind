package br.insper.insperMind.favorito;

import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/favorito")
public class FavoritoController {

    @Autowired
    private FavoritoService favoritoService;

    @GetMapping
    public Page<ResponseFavoritoDTO> listFavoritos(Pageable pageable) {
        return favoritoService.list(pageable);
    }

    @PostMapping
    public ResponseFavoritoDTO saveFavorito(@RequestBody SaveFavoritoDTO dto) {
        return favoritoService.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorito(@PathVariable Integer id) {
        favoritoService.delete(id);
    }
}
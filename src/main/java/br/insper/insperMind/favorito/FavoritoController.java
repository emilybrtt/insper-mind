package br.insper.insperMind.favorito;

import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import jakarta.validation.Valid;
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
    public Page<ResponseFavoritoDTO> listFavoritos(@RequestParam String emailUsuario, Pageable pageable) {
        return favoritoService.list(emailUsuario, pageable);
    }

    @PostMapping
    public ResponseFavoritoDTO saveFavorito(@Valid @RequestBody SaveFavoritoDTO dto){
    return favoritoService.save(dto);
    }

    @DeleteMapping("/{id}")
    public void deleteFavorito(@PathVariable Integer id, @RequestParam String emailUsuario) {
        favoritoService.delete(id, emailUsuario);
    }
}
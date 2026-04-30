package br.insper.insperMind.favorito;

import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import br.insper.insperMind.favorito.exception.FavoritoNotFoundException;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    @Autowired
    private FavoritoRepository favoritoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public ResponseFavoritoDTO save(SaveFavoritoDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());

        boolean jaExiste = favoritoRepository.existsByUsuarioAndItemIdAndTipoItemAndAtivoTrue(
                usuario,
                dto.getItemId(),
                dto.getTipoItem()
        );

        if (jaExiste) {
            throw new RuntimeException("Item já favoritado");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setId(dto.getItemId());
        favorito.setAtivo(true);

        favorito = favoritoRepository.save(favorito);

        return ResponseFavoritoDTO.toDTO(favorito);
    }

    public Page<ResponseFavoritoDTO> list(Pageable pageable) {
        return favoritoRepository.findByAtivoTrue(pageable)
                .map(ResponseFavoritoDTO::toDTO);
    }

    public void delete(Integer id) {
        Favorito favorito = favoritoRepository.findById(id)
                .orElseThrow(() -> new FavoritoNotFoundException("Favorito não encontrado"));

        favorito.setAtivo(false);
        favoritoRepository.save(favorito);
    }
}
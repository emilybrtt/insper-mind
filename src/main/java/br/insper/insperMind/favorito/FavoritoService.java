package br.insper.insperMind.favorito;

import br.insper.insperMind.eletiva.Eletiva;
import br.insper.insperMind.eletiva.EletivaService;
import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import br.insper.insperMind.favorito.exception.*;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.material.MaterialService;
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

    @Autowired
    private MaterialService materialService;

    @Autowired
    private EletivaService eletivaService;

    public Favorito get(Integer id) {
        Favorito favorito = favoritoRepository.findById(id)
                .orElseThrow(FavoritoNotFoundException::new);

        if (!favorito.getAtivo()) {
            throw new FavoritoNotFoundException();
        }

        return favorito;
    }

    public ResponseFavoritoDTO getDTO(Integer id) {
        return ResponseFavoritoDTO.toDTO(get(id));
    }

    public ResponseFavoritoDTO save(SaveFavoritoDTO dto, String emailUsuario) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);

        if (dto.getMaterialId() != null) {
            Material material = materialService.get(dto.getMaterialId());
            if (favoritoRepository.existsByUsuarioAndMaterial(usuario, material)) {
                throw new AlreadyFavoritedException("Material já foi favoritado");
            }
            favorito.setMaterial(material);
        }

        if (dto.getEletivaId() != null) {
            Eletiva eletiva = eletivaService.get(dto.getEletivaId());
            if (favoritoRepository.existsByUsuarioAndEletiva(usuario, eletiva)) {
                throw new AlreadyFavoritedException("Eletiva já foi favoritada");
            }
            favorito.setEletiva(eletiva);
        }

        favorito.setAtivo(true);
        favorito = favoritoRepository.save(favorito);
        return ResponseFavoritoDTO.toDTO(favorito);
    }

    public void delete(Integer id, String emailUsuario) {
        Favorito favorito = get(id);
        if (!favorito.getUsuario().getEmail().equals(emailUsuario)) {
            throw new FavoritoForbiddenException();
        }
        favorito.setAtivo(false);
        favoritoRepository.save(favorito);
    }

    public Page<ResponseFavoritoDTO> list(String emailUsuario, Pageable pageable) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);

        return favoritoRepository.findByUsuarioAndAtivoTrue(usuario, pageable)
                .map(ResponseFavoritoDTO::toDTO);
    }
}
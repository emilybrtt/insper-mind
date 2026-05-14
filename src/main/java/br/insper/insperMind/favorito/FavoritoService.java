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

    public ResponseFavoritoDTO save(SaveFavoritoDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());

        TipoFavorito tipo = dto.getTipoItem();

        if (tipo == null) {
            throw new InvalidItemTypeException();
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAtivo(true);
        favorito.setMaterial(null);
        favorito.setEletiva(null);

        if (tipo == TipoFavorito.MATERIAL){
            Material material = materialService.get(dto.getItemId());

            boolean jaExiste = favoritoRepository
                    .existsByUsuarioAndMaterialAndAtivoTrue(usuario, material);

            if (jaExiste) {
                throw new MaterialAlreadyFavoritedException();
            }

            favorito.setMaterial(material);
            favorito.setEletiva(null);

        } else if (tipo == TipoFavorito.ELETIVA){

            Eletiva eletiva = eletivaService.get(dto.getItemId());

            boolean jaExiste = favoritoRepository
                    .existsByUsuarioAndEletivaAndAtivoTrue(usuario, eletiva);

            if (jaExiste) {
                throw new EletivaAlreadyFavoritedException();
            }

            favorito.setEletiva(eletiva);
            favorito.setMaterial(null);
        }

        return ResponseFavoritoDTO.toDTO(favoritoRepository.save(favorito));
    }

    public Page<ResponseFavoritoDTO> list(String emailUsuario, Pageable pageable) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);

        return favoritoRepository.findByUsuarioAndAtivoTrue(usuario, pageable)
                .map(ResponseFavoritoDTO::toDTO);
    }

    public void delete(Integer id, String emailUsuario) {
        Favorito favorito = get(id);

        if (!favorito.getUsuario().getEmail().equals(emailUsuario)) {
            throw new FavoritoForbiddenException();
        }

        favorito.setAtivo(false);
        favoritoRepository.save(favorito);
    }
}
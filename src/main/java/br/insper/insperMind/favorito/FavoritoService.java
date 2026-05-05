package br.insper.insperMind.favorito;

import br.insper.insperMind.eletiva.Eletiva;
import br.insper.insperMind.eletiva.EletivaService;
import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import br.insper.insperMind.favorito.exception.EletivaAlreadyFavoritedException;
import br.insper.insperMind.favorito.exception.FavoritoNotFoundException;
import br.insper.insperMind.favorito.exception.InvalidItemTypeException;
import br.insper.insperMind.favorito.exception.MaterialAlreadyFavoritedException;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.material.MaterialService;
import br.insper.insperMind.material.exception.MaterialAlreadyExistsException;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FavoritoService {

    private static final String TIPO_MATERIAL = "material";
    private static final String TIPO_ELETIVA = "eletiva";

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
                .orElseThrow(() -> new FavoritoNotFoundException());

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

        String tipo = dto.getTipoItem();

        // validação básica
        if (!TIPO_MATERIAL.equalsIgnoreCase(tipo) &&
                !TIPO_ELETIVA.equalsIgnoreCase(tipo)) {
            throw new InvalidItemTypeException();
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAtivo(true);

        if (TIPO_MATERIAL.equalsIgnoreCase(tipo)) {

            Material material = materialService.get(dto.getItemId());

            boolean jaExiste = favoritoRepository
                    .existsByUsuarioAndMaterialAndAtivoTrue(usuario, material);

            if (jaExiste) {
                throw new MaterialAlreadyFavoritedException();
            }

            favorito.setMaterial(material);
            favorito.setEletiva(null);

        } else {

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
            throw new RuntimeException("Você não tem permissão para deletar este favorito");
        }

        favorito.setAtivo(false);
        favoritoRepository.save(favorito);
    }
}
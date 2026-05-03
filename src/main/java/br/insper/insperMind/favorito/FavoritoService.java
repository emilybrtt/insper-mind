package br.insper.insperMind.favorito;

import br.insper.insperMind.eletiva.Eletiva;
import br.insper.insperMind.eletiva.EletivaService;
import br.insper.insperMind.favorito.dto.ResponseFavoritoDTO;
import br.insper.insperMind.favorito.dto.SaveFavoritoDTO;
import br.insper.insperMind.favorito.exception.FavoritoNotFoundException;
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
                .orElseThrow(() -> new FavoritoNotFoundException("Favorito não encontrado"));

        if (!favorito.getAtivo()) {
            throw new FavoritoNotFoundException("Favorito não encontrado");
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
            throw new RuntimeException("Tipo de item inválido");
        }

        Favorito favorito = new Favorito();
        favorito.setUsuario(usuario);
        favorito.setAtivo(true);

        if (TIPO_MATERIAL.equalsIgnoreCase(tipo)) {

            Material material = materialService.get(dto.getItemId());

            boolean jaExiste = favoritoRepository
                    .existsByUsuarioAndMaterialAndAtivoTrue(usuario, material);

            if (jaExiste) {
                throw new RuntimeException("Material já favoritado");
            }

            favorito.setMaterial(material);
            favorito.setEletiva(null);

        } else {

            Eletiva eletiva = eletivaService.get(dto.getItemId());

            boolean jaExiste = favoritoRepository
                    .existsByUsuarioAndEletivaAndAtivoTrue(usuario, eletiva);

            if (jaExiste) {
                throw new RuntimeException("Eletiva já favoritada");
            }

            favorito.setEletiva(eletiva);
            favorito.setMaterial(null);
        }

        return ResponseFavoritoDTO.toDTO(favoritoRepository.save(favorito));
    }

    public Page<ResponseFavoritoDTO> list(Pageable pageable) {
        return favoritoRepository.findByAtivoTrue(pageable)
                .map(ResponseFavoritoDTO::toDTO);
    }

    public void delete(Integer id) {
        Favorito favorito = get(id);

        favorito.setAtivo(false);
        favoritoRepository.save(favorito);
    }
}
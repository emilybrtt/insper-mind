package br.insper.insperMind.material;

import br.insper.insperMind.common.FileStorageService;
import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.disciplina.DisciplinaService;
import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
import br.insper.insperMind.material.exception.InvalidFileException;
import br.insper.insperMind.material.exception.MaterialAlreadyExistsException;
import br.insper.insperMind.material.exception.MaterialForbiddenException;
import br.insper.insperMind.material.exception.MaterialNotFoundException;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioRepository;
import br.insper.insperMind.usuario.UsuarioService;
import br.insper.insperMind.usuario.exception.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private FileStorageService fileStorageService;

    public Material get(Integer id) {
        return materialRepository.findById(id)
                .filter(Material::getAtivo)
                .orElseThrow(MaterialNotFoundException::new);
    }

    public ResponseMaterialDTO getDTO(Integer id) {
        return ResponseMaterialDTO.toDTO(get(id));
    }

    public ResponseMaterialDTO save(SaveMaterialDTO dto, String emailUsuario) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);
        Disciplina disciplina = disciplinaService.get(dto.getDisciplinaId());

        if (materialRepository.existsByTituloAndAtivoTrue(dto.getTitulo())) {
            throw new MaterialAlreadyExistsException();
        }

        Material material = Material.toModel(dto, usuario, disciplina);
        material = materialRepository.save(material);

        return ResponseMaterialDTO.toDTO(material);
    }

    public Page<ResponseMaterialDTO> list(Pageable pageable) {
        return materialRepository.findByAtivoTrue(pageable)
                .map(ResponseMaterialDTO::toDTO);
    }

    public Page<ResponseMaterialDTO> list(Integer cursoId, Integer disciplinaId, String emailUsuario, String tipo, Pageable pageable) {
        if (emailUsuario != null) {
            return materialRepository.findByUsuarioEmailAndAtivoTrue(emailUsuario, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }
        if (cursoId != null) {
            return materialRepository.findByDisciplinaSemestreCursoIdAndTipo(cursoId, tipo, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }
        if (disciplinaId != null) {
            return materialRepository.findByDisciplinaIdAndTipo(disciplinaId, tipo, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }
        if (tipo != null) {
            return materialRepository.findByTipo(tipo, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }
        return materialRepository.findAll(pageable).map(ResponseMaterialDTO::toDTO);
    }

    public ResponseMaterialDTO edit(Integer id, EditMaterialDTO dto, String emailUsuario) {
        Material material = get(id);

        validateOwner(material, emailUsuario);

        Disciplina disciplina = (dto.getDisciplinaId() != null)
                ? disciplinaService.get(dto.getDisciplinaId())
                : null;

        material.update(dto, disciplina);

        return ResponseMaterialDTO.toDTO(materialRepository.save(material));
    }

    public void delete(Integer id, String emailUsuario) {
        Material material = get(id);

        validateOwner(material, emailUsuario);

        material.setAtivo(false);
        materialRepository.save(material);
    }

    private void validateOwner(Material material, String emailUsuario) {
        if (!material.getUsuario().getEmail().equals(emailUsuario)) {
            throw new MaterialForbiddenException();
        }
    }

    public ResponseMaterialDTO curtir(Integer id, String emailUsuario) {
        Material material = get(id);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(UsuarioNotFoundException::new);

        if (material.getUsuariosQueCurtiram().contains(usuario)) {
            material.getUsuariosQueCurtiram().remove(usuario);
            material.setCurtidas(material.getCurtidas() - 1);
        } else {
            material.getUsuariosQueCurtiram().add(usuario);
            material.setCurtidas(material.getCurtidas() + 1);
        }
        material = materialRepository.save(material);
        return ResponseMaterialDTO.toDTO(material);
    }


    public ResponseMaterialDTO salvarArquivo(MultipartFile file, Integer disciplinaId, String emailUsuario) {
        if (file.isEmpty()) throw new InvalidFileException();

        String nomeArquivo = fileStorageService.salvarArquivo(file);
        Usuario usuario = usuarioService.findByEmail(emailUsuario);
        Disciplina disciplina = disciplinaService.get(disciplinaId);

        Material material = Material.criarDoArquivo(nomeArquivo, usuario, disciplina);
        material = materialRepository.save(material);
        return ResponseMaterialDTO.toDTO(material);
    }
}
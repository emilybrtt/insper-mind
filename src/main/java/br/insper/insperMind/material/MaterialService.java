package br.insper.insperMind.material;

import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.disciplina.DisciplinaService;
import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
import br.insper.insperMind.material.exception.MaterialAlreadyExistsException;
import br.insper.insperMind.material.exception.MaterialForbiddenException;
import br.insper.insperMind.material.exception.MaterialNotFoundException;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DisciplinaService disciplinaService;

    public Material get(Integer id) {
        return materialRepository.findById(id)
                .filter(Material::getAtivo)
                .orElseThrow(MaterialNotFoundException::new);
    }

    public ResponseMaterialDTO getDTO(Integer id) {
        return ResponseMaterialDTO.toDTO(get(id));
    }

    public ResponseMaterialDTO save(SaveMaterialDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());
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

    public Page<ResponseMaterialDTO> list(Integer disciplinaId, String emailUsuario, String tipo, Pageable pageable) {
        if (disciplinaId != null) {
            return materialRepository.findByAtivoTrueAndDisciplinaId(disciplinaId, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }

        if (emailUsuario != null) {
            return materialRepository.findByAtivoTrueAndUsuarioEmail(emailUsuario, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }

        if (tipo != null) {
            TipoMaterial tipoMaterial = TipoMaterial.valueOf(tipo.toUpperCase());
            return materialRepository.findByAtivoTrueAndTipo(tipoMaterial, pageable)
                    .map(ResponseMaterialDTO::toDTO);
        }

        return list(pageable);
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
}
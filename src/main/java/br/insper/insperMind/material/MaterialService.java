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
                .orElseThrow(() -> new MaterialNotFoundException());
    }

    public ResponseMaterialDTO getDTO(Integer id) {
        return ResponseMaterialDTO.toDTO(get(id));
    }

    public ResponseMaterialDTO save(SaveMaterialDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());
        Disciplina disciplina = disciplinaService.get(dto.getDisciplinaId());

        if (materialRepository.existsByTitulo(dto.getTitulo())) {
            throw new MaterialAlreadyExistsException();
        }
        Material material = Material.toModel(dto, usuario, disciplina);

        return ResponseMaterialDTO.toDTO(materialRepository.save(material));
    }

    public Page<ResponseMaterialDTO> list(Pageable pageable) {
        return materialRepository.findByAtivoTrue(pageable)
                .map(ResponseMaterialDTO::toDTO);
    }

    public ResponseMaterialDTO edit(Integer id, EditMaterialDTO dto, String emailUsuario) {
        Material material = get(id);

        validateOwner(material, emailUsuario);

        Disciplina disciplina = (dto.getDisciplinaId() != null) ? disciplinaService.get(dto.getDisciplinaId()) : null;
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

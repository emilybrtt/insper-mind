package br.insper.insperMind.material;

import br.insper.insperMind.curso.Curso;
import br.insper.insperMind.curso.CursoService;
import br.insper.insperMind.material.dto.EditMaterialDTO;
import br.insper.insperMind.material.dto.ResponseMaterialDTO;
import br.insper.insperMind.material.dto.SaveMaterialDTO;
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
    private CursoService cursoService;

    public Material get(Integer id) {
        return materialRepository.findById(id)
                .filter(Material::getAtivo)
                .orElseThrow(() -> new MaterialNotFoundException("Material não encontrado"));
    }

    public ResponseMaterialDTO getDTO(Integer id) {
        return ResponseMaterialDTO.toDTO(get(id));
    }

    public ResponseMaterialDTO save(SaveMaterialDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());
        Curso curso = cursoService.get(dto.getCursoId());

        if (materialRepository.existsByTitulo(dto.getTitulo())) {
            throw new MaterialNotFoundException("Material já cadastrado");
        }
        Material material = Material.toModel(dto, usuario, curso);

        return ResponseMaterialDTO.toDTO(materialRepository.save(material));
    }

    public Page<ResponseMaterialDTO> list(Pageable pageable) {
        return materialRepository.findByAtivoTrue(pageable)
                .map(ResponseMaterialDTO::toDTO);
    }

    public ResponseMaterialDTO edit(Integer id, EditMaterialDTO dto) {
        Material material = get(id);

        Curso curso = (dto.getCursoId() != null) ? cursoService.get(dto.getCursoId()) : null;
        material.update(dto, curso);

        return ResponseMaterialDTO.toDTO(materialRepository.save(material));
    }

    public void delete(Integer id) {
        Material material = get(id);
        material.setAtivo(false);
        materialRepository.save(material);
    }
}
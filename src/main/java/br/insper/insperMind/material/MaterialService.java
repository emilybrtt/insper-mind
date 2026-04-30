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

    public ResponseMaterialDTO save(SaveMaterialDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());
        Curso curso = cursoService.findModelById(dto.getCursoId());

        Material material = new Material();
        material.setTitulo(dto.getTitulo());
        material.setDescricao(dto.getDescricao());
        material.setLink(dto.getLink());
        material.setTipo(TipoMaterial.valueOf(dto.getTipo()));
        material.setUsuario(usuario);
        material.setCurso(curso);
        material.setAtivo(true);

        material = materialRepository.save(material);

        return ResponseMaterialDTO.toDTO(material);
    }

    public Page<ResponseMaterialDTO> list(Pageable pageable) {
        return materialRepository.findByAtivoTrue(pageable)
                .map(ResponseMaterialDTO::toDTO);
    }

    public ResponseMaterialDTO findById(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material não encontrado"));

        return ResponseMaterialDTO.toDTO(material);
    }

    public Material findEntityById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material não encontrado"));
    }

    public ResponseMaterialDTO edit(Integer id, EditMaterialDTO dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material não encontrado"));

        if (dto.getTitulo() != null) {
            material.setTitulo(dto.getTitulo());
        }

        if (dto.getDescricao() != null) {
            material.setDescricao(dto.getDescricao());
        }

        if (dto.getLink() != null) {
            material.setLink(dto.getLink());
        }

        if (dto.getTipo() != null) {
            material.setTipo(TipoMaterial.valueOf(dto.getTipo()));
        }

        if (dto.getCursoId() != null) {
            Curso curso = cursoService.findModelById(dto.getCursoId());
            material.setCurso(curso);
        }

        if (dto.getAtivo() != null) {
            material.setAtivo(dto.getAtivo());
        }

        material = materialRepository.save(material);

        return ResponseMaterialDTO.toDTO(material);
    }

    public void delete(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new MaterialNotFoundException("Material não encontrado"));

        material.setAtivo(false);
        materialRepository.save(material);
    }
}
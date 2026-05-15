package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.EditComentarioDTO;
import br.insper.insperMind.comentario.dto.ResponseComentarioDTO;
import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import br.insper.insperMind.comentario.exception.ComentarioForbiddenException;
import br.insper.insperMind.comentario.exception.ComentarioNotFoundException;
import br.insper.insperMind.comentario.exception.ComentarioSemVinculoException;
import br.insper.insperMind.disciplina.Disciplina;
import br.insper.insperMind.disciplina.DisciplinaService;
import br.insper.insperMind.material.Material;
import br.insper.insperMind.material.MaterialService;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioRepository;
import br.insper.insperMind.usuario.UsuarioService;
import br.insper.insperMind.usuario.exception.UsuarioNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private DisciplinaService disciplinaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MaterialService materialService;

    public Comentario get(Integer id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(ComentarioNotFoundException::new);

        if (!comentario.getAtivo()) {
            throw new ComentarioNotFoundException();
        }

        return comentario;
    }

    public ResponseComentarioDTO getDTO(Integer id) {
        return ResponseComentarioDTO.toDTO(get(id));
    }

    public ResponseComentarioDTO save(SaveComentarioDTO dto, String emailUsuario) {
        Usuario usuario = usuarioService.findByEmail(emailUsuario);

        Disciplina disciplina = null;
        Material material = null;
        Comentario comentarioPai = null;

        if (dto.getIdDisciplina() != null) {
            disciplina = disciplinaService.get(dto.getIdDisciplina());
        }

        if (dto.getIdMaterial() != null) {
            material = materialService.get(dto.getIdMaterial());
        }

        if (dto.getComentarioPaiId() != null) {
            comentarioPai = get(dto.getComentarioPaiId());

            if (disciplina == null) {
                disciplina = comentarioPai.getDisciplina();
            }

            if (material == null) {
                material = comentarioPai.getMaterial();
            }
        }

        if (disciplina == null && material == null) {
            throw new ComentarioSemVinculoException();
        }

        Comentario comentario = new Comentario();
        comentario.setComentario(dto.getComentario());
        comentario.setCurtidas(0);
        comentario.setUsuario(usuario);
        comentario.setDisciplina(disciplina);
        comentario.setMaterial(material);
        comentario.setComentarioPai(comentarioPai);
        comentario.setAtivo(true);

        comentario = comentarioRepository.save(comentario);

        return ResponseComentarioDTO.toDTO(comentario);
    }

    public Page<ResponseComentarioDTO> list(Integer idDisciplina,
                                            Integer idMaterial,
                                            Integer comentarioPaiId,
                                            Pageable pageable) {

        Page<Comentario> comentarios;

        if (comentarioPaiId != null) {
            comentarios = comentarioRepository.findByAtivoTrueAndComentarioPaiId(comentarioPaiId, pageable);
        } else if (idMaterial != null) {
            comentarios = comentarioRepository.findByAtivoTrueAndMaterialId(idMaterial, pageable);
        } else if (idDisciplina != null) {
            comentarios = comentarioRepository.findByAtivoTrueAndDisciplinaId(idDisciplina, pageable);
        } else {
            comentarios = comentarioRepository.findByAtivoTrueAndComentarioPaiIsNull(pageable);
        }

        return comentarios.map(ResponseComentarioDTO::toDTO);
    }

    public ResponseComentarioDTO edit(Integer id, EditComentarioDTO dto, String emailUsuario) {
        Comentario comentario = get(id);
        validateOwner(comentario, emailUsuario);

        if (dto.getComentario() != null) {
            comentario.setComentario(dto.getComentario());
        }

        if (dto.getAtivo() != null) {
            comentario.setAtivo(dto.getAtivo());
        }

        comentario = comentarioRepository.save(comentario);

        return ResponseComentarioDTO.toDTO(comentario);
    }

    public ResponseComentarioDTO curtir(Integer id, String emailUsuario) {
        Comentario comentario = get(id);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(UsuarioNotFoundException::new);

        if (comentario.getUsuariosQueCurtiram().contains(usuario)) {
            comentario.getUsuariosQueCurtiram().remove(usuario);
            comentario.setCurtidas(comentario.getCurtidas() - 1);
        } else {
            comentario.getUsuariosQueCurtiram().add(usuario);
            comentario.setCurtidas(comentario.getCurtidas() + 1);
        }
        comentario = comentarioRepository.save(comentario);
        return ResponseComentarioDTO.toDTO(comentario);
    }

    public void delete(Integer id, String emailUsuario) {
        Comentario comentario = get(id);
        validateOwner(comentario, emailUsuario);

        comentario.setAtivo(false);
        comentarioRepository.save(comentario);
    }

    private void validateOwner(Comentario comentario, String emailUsuario) {
        if (comentario.getUsuario() == null || !comentario.getUsuario().getEmail().equals(emailUsuario)) {
            throw new ComentarioForbiddenException();
        }
    }
}
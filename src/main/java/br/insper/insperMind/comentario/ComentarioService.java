package br.insper.insperMind.comentario;

import br.insper.insperMind.comentario.dto.EditComentarioDTO;
import br.insper.insperMind.comentario.dto.ResponseComentarioDTO;
import br.insper.insperMind.comentario.dto.SaveComentarioDTO;
import br.insper.insperMind.comentario.exception.ComentarioNotFoundException;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioService;
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

    public ResponseComentarioDTO save(SaveComentarioDTO dto) {
        Usuario usuario = usuarioService.findByEmail(dto.getEmailUsuario());

        Comentario comentario = Comentario.toModel(dto, usuario);
        comentario = comentarioRepository.save(comentario);

        return ResponseComentarioDTO.toDTO(comentario);
    }

    public Page<ResponseComentarioDTO> list(Pageable pageable) {
        return comentarioRepository.findByAtivoTrue(pageable)
                .map(ResponseComentarioDTO::toDTO);
    }

    public ResponseComentarioDTO edit(Integer id, EditComentarioDTO dto) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentário não encontrado"));

        if (dto.getComentario() != null) {
            comentario.setComentario(dto.getComentario());
        }

        if (dto.getAtivo() != null) {
            comentario.setAtivo(dto.getAtivo());
        }

        comentario = comentarioRepository.save(comentario);

        return ResponseComentarioDTO.toDTO(comentario);
    }

    public ResponseComentarioDTO curtir(Integer id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentário não encontrado"));

        if (comentario.getCurtidas() == null) {
            comentario.setCurtidas(0);
        }

        comentario.setCurtidas(comentario.getCurtidas() + 1);
        comentario = comentarioRepository.save(comentario);

        return ResponseComentarioDTO.toDTO(comentario);
    }

    public void delete(Integer id) {
        Comentario comentario = comentarioRepository.findById(id)
                .orElseThrow(() -> new ComentarioNotFoundException("Comentário não encontrado"));

        comentario.setAtivo(false);
        comentarioRepository.save(comentario);
    }
}
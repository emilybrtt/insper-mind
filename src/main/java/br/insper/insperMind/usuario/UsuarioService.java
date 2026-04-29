package br.insper.insperMind.usuario;

import br.insper.insperMind.usuario.dto.EditUsuarioDTO;
import br.insper.insperMind.usuario.dto.ResponseUsuarioDTO;
import br.insper.insperMind.usuario.dto.SaveUsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ResponseUsuarioDTO save(SaveUsuarioDTO dto) {
        Usuario usuario = Usuario.toModel(dto);
        usuario = usuarioRepository.save(usuario);
        return ResponseUsuarioDTO.toDTO(usuario);
    }

    public Page<ResponseUsuarioDTO> list(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(ResponseUsuarioDTO::toDTO);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public ResponseUsuarioDTO getDto(String email) {
        return ResponseUsuarioDTO.toDTO(findByEmail(email));
    }

    public ResponseUsuarioDTO update(String email, EditUsuarioDTO dto) {
        Usuario usuario = findByEmail(email);
        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getSenha() != null) usuario.setSenha(dto.getSenha());
        return ResponseUsuarioDTO.toDTO(usuarioRepository.save(usuario));
    }
}
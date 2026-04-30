package br.insper.insperMind.usuario;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.insper.insperMind.usuario.dto.EditUsuarioDTO;
import br.insper.insperMind.usuario.dto.LoginUsuarioDTO;
import br.insper.insperMind.usuario.dto.ResponseUsuarioDTO;
import br.insper.insperMind.usuario.dto.SaveUsuarioDTO;
import br.insper.insperMind.usuario.exception.UsuarioNotFoundException;
import lombok.extern.java.Log;
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

        String bcryptHashString = BCrypt.withDefaults().hashToString(12, dto.getSenha().toCharArray()); // Criptografa senha
        usuario.setSenha(bcryptHashString);

        usuario = usuarioRepository.save(usuario);
        return ResponseUsuarioDTO.toDTO(usuario);
    }

    public Page<ResponseUsuarioDTO> list(Pageable pageable) {
        return usuarioRepository.findAll(pageable)
                .map(ResponseUsuarioDTO::toDTO);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));
    }

    public ResponseUsuarioDTO getDto(String email) {
        return ResponseUsuarioDTO.toDTO(findByEmail(email));
    }

    public ResponseUsuarioDTO update(String email, EditUsuarioDTO dto) {
        Usuario usuario = findByEmail(email);

        if (dto.getNome() != null) {
            usuario.setNome(dto.getNome());
        }
        if (dto.getSenha() != null) {
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, dto.getSenha().toCharArray());
            usuario.setSenha(bcryptHashString);
        }
        if (dto.getEmail() != null) {
            usuario.setEmail(dto.getEmail());
        }

        return ResponseUsuarioDTO.toDTO(usuarioRepository.save(usuario));
    }

    public void delete(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNotFoundException("Usuário não encontrado"));

        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public boolean authenticate(LoginUsuarioDTO loginDTO) { // método de autenticação
        Usuario usuario = findByEmail(loginDTO.getEmail());
        BCrypt.Result result = BCrypt.verifyer().verify(loginDTO.getSenha().toCharArray(), usuario.getSenha());
        return result.verified && usuario.getAtivo();
    }
}
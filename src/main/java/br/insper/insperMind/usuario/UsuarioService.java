package br.insper.insperMind.usuario;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.insper.insperMind.common.JwtUtil;
import br.insper.insperMind.usuario.dto.EditUsuarioDTO;
import br.insper.insperMind.usuario.dto.LoginUsuarioDTO;
import br.insper.insperMind.usuario.dto.ResponseUsuarioDTO;
import br.insper.insperMind.usuario.dto.SaveUsuarioDTO;
import br.insper.insperMind.usuario.exception.UnauthorizedException;
import br.insper.insperMind.usuario.exception.UsuarioAlreadyExistsException;
import br.insper.insperMind.usuario.exception.UsuarioNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public Usuario get(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);

        if (!usuario.getAtivo()) {
            throw new UsuarioNotFoundException();
        }

        return usuario;
    }

    public ResponseUsuarioDTO getDTO(Integer id) {
        return ResponseUsuarioDTO.toDTO(get(id));
    }

    public Usuario findByEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNotFoundException::new);

        if (!usuario.getAtivo()) {
            throw new UsuarioNotFoundException();
        }

        return usuario;
    }

    public ResponseUsuarioDTO save(SaveUsuarioDTO dto) {
        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new UsuarioAlreadyExistsException();
        }

        Usuario usuario = Usuario.toModel(dto);

        String bcryptHashString = BCrypt.withDefaults()
                .hashToString(12, dto.getSenha().toCharArray());

        usuario.setSenha(bcryptHashString);
        usuario.setAtivo(true);

        usuario = usuarioRepository.save(usuario);
        return ResponseUsuarioDTO.toDTO(usuario);
    }

    public Page<ResponseUsuarioDTO> list(Pageable pageable) {
        return usuarioRepository.findByAtivoTrue(pageable)
                .map(ResponseUsuarioDTO::toDTO);
    }

    public ResponseUsuarioDTO update(Integer id, EditUsuarioDTO dto) {
        Usuario usuario = get(id);

        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            usuario.setNome(dto.getNome());
        }

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            String bcryptHashString = BCrypt.withDefaults()
                    .hashToString(12, dto.getSenha().toCharArray());
            usuario.setSenha(bcryptHashString);
        }

        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            if (!dto.getEmail().equals(usuario.getEmail()) && usuarioRepository.existsByEmail(dto.getEmail())) {
                throw new UsuarioAlreadyExistsException();
            }
            usuario.setEmail(dto.getEmail());
        }

        usuario = usuarioRepository.save(usuario);
        return ResponseUsuarioDTO.toDTO(usuario);
    }

    public void delete(Integer id) {
        Usuario usuario = get(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    public boolean authenticate(LoginUsuarioDTO loginDTO) {
        Usuario usuario = findByEmail(loginDTO.getEmail());
        BCrypt.Result result = BCrypt.verifyer()
                .verify(loginDTO.getSenha().toCharArray(), usuario.getSenha());

        return result.verified && usuario.getAtivo();
    }

    public String authenticateAndGenerateToken(LoginUsuarioDTO loginDTO) {
        Usuario usuario = findByEmail(loginDTO.getEmail());
        BCrypt.Result result = BCrypt.verifyer()
                .verify(loginDTO.getSenha().toCharArray(), usuario.getSenha());
        if (!result.verified || !usuario.getAtivo()) {
            throw new UnauthorizedException();
        }
        return jwtUtil.generateToken(usuario.getEmail(), usuario.getId(), usuario.getRole());
    }

    public void validateOwner(Integer id, String emailUsuario) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return;
        }
        Usuario usuario = get(id);
        if (!usuario.getEmail().equals(emailUsuario)) {
            throw new UnauthorizedException();
        }
    }

    public ResponseUsuarioDTO setAtivo(Integer id, Boolean ativo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNotFoundException::new);
        usuario.setAtivo(ativo);
        return ResponseUsuarioDTO.toDTO(usuarioRepository.save(usuario));
    }
}
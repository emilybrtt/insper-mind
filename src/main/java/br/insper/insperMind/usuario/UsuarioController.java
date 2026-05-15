package br.insper.insperMind.usuario;

import br.insper.insperMind.usuario.dto.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public Page<ResponseUsuarioDTO> listUsuarios(Pageable pageable) {
        return usuarioService.list(pageable);
    }

    @GetMapping("/{id}")
    public ResponseUsuarioDTO getUsuario(@PathVariable Integer id) {
        return usuarioService.getDTO(id);
    }

    @PostMapping
    public ResponseUsuarioDTO saveUsuario(@Valid @RequestBody SaveUsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    @PatchMapping("/{id}")
    public ResponseUsuarioDTO updateUsuario(@PathVariable Integer id,
                                            @Valid @RequestBody EditUsuarioDTO dto) {
        return usuarioService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteUsuario(@PathVariable Integer id) {
        usuarioService.delete(id);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponseDTO> login(@Valid @RequestBody LoginUsuarioDTO loginDTO) {
        String token = usuarioService.authenticateAndGenerateToken(loginDTO);
        return ResponseEntity.ok(new TokenResponseDTO(token));
    }
}
package br.insper.insperMind.usuario;

import br.insper.insperMind.usuario.dto.EditUsuarioDTO;
import br.insper.insperMind.usuario.dto.ResponseUsuarioDTO;
import br.insper.insperMind.usuario.dto.SaveUsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/{email}")
    public ResponseUsuarioDTO getUsuario(@PathVariable String email) {
        return usuarioService.getDto(email);
    }

    @PostMapping
    public ResponseUsuarioDTO saveUsuario(@RequestBody SaveUsuarioDTO dto) {
        return usuarioService.save(dto);
    }

    @PatchMapping("/{email}")
    public ResponseUsuarioDTO updateUsuario(@PathVariable String email, @RequestBody EditUsuarioDTO dto) {
        return usuarioService.update(email, dto);
    }
}
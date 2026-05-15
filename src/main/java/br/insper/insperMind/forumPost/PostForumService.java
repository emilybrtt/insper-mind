package br.insper.insperMind.forumPost;

import br.insper.insperMind.forumPost.dto.SavePostForumDTO;
import br.insper.insperMind.forumPost.dto.EditPostForumDTO;
import br.insper.insperMind.forumPost.dto.ResponsePostForumDTO;
import br.insper.insperMind.usuario.Usuario;
import br.insper.insperMind.usuario.UsuarioRepository;
import br.insper.insperMind.usuario.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import br.insper.insperMind.forumPost.exception.PostNotFoundException;
import br.insper.insperMind.forumPost.exception.ForbiddenException;

import java.time.LocalDateTime;

@Service
public class PostForumService {
    @Autowired
    private PostForumRepository postRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ResponsePostForumDTO save(SavePostForumDTO dto, String emailUsuario) {
        PostForum post = new PostForum();
        post.setTitulo(dto.getTitulo());
        post.setConteudo(dto.getConteudo());
        post.setCategoria(dto.getCategoria());
        post.setUsuario(usuarioService.findByEmail(emailUsuario));
        post = postRepository.save(post);
        return ResponsePostForumDTO.toDTO(post);
    }

    public Page<ResponsePostForumDTO> list(CategoriaForum categoria, Pageable pageable) {
        if (categoria != null) {
            return postRepository.findByAtivoAndCategoria(true, categoria, pageable)
                    .map(ResponsePostForumDTO::toDTO);
        }
        return postRepository.findByAtivoTrue(pageable).map(ResponsePostForumDTO::toDTO);
    }

    public ResponsePostForumDTO getDTO(Integer id) {
        PostForum post = postRepository.findById(id)
                .filter(PostForum::getAtivo)
                .orElseThrow(PostNotFoundException::new);
        return ResponsePostForumDTO.toDTO(post);
    }

    public ResponsePostForumDTO edit(Integer id, EditPostForumDTO dto, String emailUsuario) {
        PostForum post = postRepository.findById(id)
                .filter(PostForum::getAtivo)
                .orElseThrow(PostNotFoundException::new);
        if (!post.getUsuario().getEmail().equals(emailUsuario)) {
            throw new ForbiddenException();
        }
        if (dto.getTitulo() != null) post.setTitulo(dto.getTitulo());
        if (dto.getConteudo() != null) post.setConteudo(dto.getConteudo());
        post.setDataAtualizacao(LocalDateTime.now());
        post = postRepository.save(post);
        return ResponsePostForumDTO.toDTO(post);
    }

    public void delete(Integer id, String emailUsuario) {
        PostForum post = postRepository.findById(id)
                .filter(PostForum::getAtivo)
                .orElseThrow(PostNotFoundException::new);
        if (!post.getUsuario().getEmail().equals(emailUsuario)) {
            throw new ForbiddenException();
        }
        post.setAtivo(false);
        postRepository.save(post);
    }

    public ResponsePostForumDTO curtir(Integer id, String emailUsuario) {
        PostForum post = postRepository.findById(id)
                .filter(PostForum::getAtivo)
                .orElseThrow(PostNotFoundException::new);
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow();
        if (post.getUsuariosQueCurtiram().contains(usuario)) {
            post.getUsuariosQueCurtiram().remove(usuario);
            post.setCurtidas(post.getCurtidas() - 1);
        } else {
            post.getUsuariosQueCurtiram().add(usuario);
            post.setCurtidas(post.getCurtidas() + 1);
        }
        post = postRepository.save(post);
        return ResponsePostForumDTO.toDTO(post);
    }
}
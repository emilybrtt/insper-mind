package br.insper.insperMind.forumPost;

import br.insper.insperMind.forumPost.dto.SavePostForumDTO;
import br.insper.insperMind.forumPost.dto.EditPostForumDTO;
import br.insper.insperMind.forumPost.dto.ResponsePostForumDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/forum")
public class PostForumController {
    @Autowired
    private PostForumService postService;

    @GetMapping
    public Page<ResponsePostForumDTO> list(@RequestParam(required = false) CategoriaForum categoria,
                                           Pageable pageable) {
        return postService.list(categoria, pageable);
    }

    @GetMapping("/{id}")
    public ResponsePostForumDTO get(@PathVariable Integer id) {
        return postService.getDTO(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponsePostForumDTO save(@Valid @RequestBody SavePostForumDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.save(dto, email);
    }

    @PutMapping("/{id}")
    public ResponsePostForumDTO edit(@PathVariable Integer id, @Valid @RequestBody EditPostForumDTO dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.edit(id, dto, email);
    }

    @PatchMapping("/{id}/curtir")
    public ResponsePostForumDTO curtir(@PathVariable Integer id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return postService.curtir(id, email);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            postService.adminDelete(id);
        } else {
            postService.delete(id, auth.getName());
        }
    }
}
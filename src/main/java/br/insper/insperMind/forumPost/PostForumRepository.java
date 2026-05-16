package br.insper.insperMind.forumPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostForumRepository extends JpaRepository<PostForum, Integer> {
    Page<PostForum> findByAtivoAndCategoria(Boolean ativo, CategoriaForum categoria, Pageable pageable);
    Page<PostForum> findByAtivoTrue(Pageable pageable);
}
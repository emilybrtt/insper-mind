package br.insper.insperMind.forumPost.exception;

import br.insper.insperMind.common.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@ControllerAdvice
public class PostForumErrorHandling {

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handlePostNotFound(PostNotFoundException ex, HttpServletRequest request) {
        ErrorDTO dto = new ErrorDTO();
        dto.setMensagem("Post não encontrado");
        dto.setData(LocalDateTime.now());
        dto.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        dto.setCodigoErro("POST_NOT_FOUND");
        dto.setPath(request.getRequestURI());
        return dto;
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleForbidden(ForbiddenException ex, HttpServletRequest request) {
        ErrorDTO dto = new ErrorDTO();
        dto.setMensagem("Acesso negado");
        dto.setData(LocalDateTime.now());
        dto.setCodigoHttp(HttpStatus.FORBIDDEN.value());
        dto.setCodigoErro("FORBIDDEN");
        dto.setPath(request.getRequestURI());
        return dto;
    }
}
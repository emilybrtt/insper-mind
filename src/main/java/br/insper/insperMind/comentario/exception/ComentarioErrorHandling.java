package br.insper.insperMind.comentario.exception;

import br.insper.insperMind.common.ErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class ComentarioErrorHandling {

    @ExceptionHandler(ComentarioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleComentarioNotFoundException(ComentarioNotFoundException ex,
                                                      HttpServletRequest request) {
        log.error("Comentario nao encontrado");

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        errorDTO.setCodigoErro("COMENTARIO_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(ComentarioSemVinculoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleComentarioSemVinculoException(ComentarioSemVinculoException ex,
                                                        HttpServletRequest request) {
        log.error("Comentario sem vinculo");

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("COMENTARIO_INVALIDO");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(ComentarioForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleComentarioForbiddenException(ComentarioForbiddenException ex,
                                                       HttpServletRequest request) {
        log.error("Comentario sem permissao");

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.FORBIDDEN.value());
        errorDTO.setCodigoErro("COMENTARIO_FORBIDDEN");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }
}
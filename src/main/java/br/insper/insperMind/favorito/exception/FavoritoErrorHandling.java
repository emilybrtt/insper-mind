package br.insper.insperMind.favorito.exception;

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
public class FavoritoErrorHandling {

    @ExceptionHandler(FavoritoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleFavoritoNotFoundException(FavoritoNotFoundException ex,
                                                 HttpServletRequest request) {

        log.error("Favorito nao encontrado");

        ErrorDTO errorDTO =  new ErrorDTO();
        errorDTO.setMensagem("Favorito nao encontrado");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        errorDTO.setCodigoErro("FAVORITO_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return  errorDTO;

    }

    @ExceptionHandler(AlreadyFavoritedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleAlreadyFavoritedException(AlreadyFavoritedException ex,
                                                            HttpServletRequest request) {

        log.error("Material já favoritado");

        ErrorDTO errorDTO =  new ErrorDTO();
        errorDTO.setMensagem("Item já favoritado");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.CONFLICT.value());
        errorDTO.setCodigoErro("ALREADY_FAVORITED");
        errorDTO.setPath(request.getRequestURI());
        return  errorDTO;

    }

    @ExceptionHandler(InvalidItemTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleInvalidItemTypeException(InvalidItemTypeException ex,
                                                            HttpServletRequest request) {

        log.error("Esse item não existe");

        ErrorDTO errorDTO =  new ErrorDTO();
        errorDTO.setMensagem("Esse item não existe");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("INVALID_ITEM_TYPE");
        errorDTO.setPath(request.getRequestURI());
        return  errorDTO;

    }

    @ExceptionHandler(FavoritoForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleFavoritoForbiddenException(FavoritoForbiddenException ex,
                                                     HttpServletRequest request) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.FORBIDDEN.value());
        errorDTO.setCodigoErro("FAVORITO_FORBIDDEN");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }
}

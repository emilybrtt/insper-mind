package br.insper.insperMind.material.exception;

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
public class MaterialErrorHandling {

    @ExceptionHandler(MaterialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleMaterialNotFoundException(MaterialNotFoundException ex,
                                                    HttpServletRequest request) {
        log.error("Material nao encontrado", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        errorDTO.setCodigoErro("MATERIAL_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(MaterialAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleMaterialAlreadyExistsException(MaterialAlreadyExistsException ex,
                                                         HttpServletRequest request) {
        log.error("Material ja cadastrado", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.CONFLICT.value());
        errorDTO.setCodigoErro("MATERIAL_ALREADY_EXISTS");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(MaterialForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDTO handleMaterialForbiddenException(MaterialForbiddenException ex,
                                                     HttpServletRequest request) {
        log.error("Material sem permissao", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.FORBIDDEN.value());
        errorDTO.setCodigoErro("MATERIAL_FORBIDDEN");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(MaterialInvalidTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleMaterialInvalidTypeException(MaterialInvalidTypeException ex,
                                                       HttpServletRequest request) {
        log.error("Tipo de material invalido", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("MATERIAL_INVALID_TYPE");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }
}
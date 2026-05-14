package br.insper.insperMind.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.stream.LangCollectors.collect;

@ControllerAdvice
@Slf4j
public class GlobalErrorHandling {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {

        String mensagem = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(","));

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(mensagem);
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("INPUT_ERROR");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex,
                                                              HttpServletRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem("Parametro invalido");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("INPUT_ERROR");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleHttpMessageNotReadableException(HttpMessageNotReadableException ex,
                                                          HttpServletRequest request) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem("Corpo da requisicao invalido");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("INPUT_ERROR");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDTO handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Erro inesperado", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem("Erro inesperado");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDTO.setCodigoErro("INTERNAL_ERROR");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleConstraintViolationException(
            ConstraintViolationException ex,
            HttpServletRequest request) {

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem("Dados invalidos");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("INPUT_ERROR");
        errorDTO.setPath(request.getRequestURI());

        return errorDTO;
    }
}
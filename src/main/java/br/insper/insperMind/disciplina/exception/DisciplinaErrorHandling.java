package br.insper.insperMind.disciplina.exception;

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
public class DisciplinaErrorHandling {

    @ExceptionHandler(DisciplinaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleDisciplinaNotFoundException(DisciplinaNotFoundException ex,
                                                      HttpServletRequest request) {
        log.error("Disciplina nao encontrada", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        errorDTO.setCodigoErro("DISCIPLINA_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(DisciplinaAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleDisciplinaAlreadyExistsException(DisciplinaAlreadyExistsException ex,
                                                           HttpServletRequest request) {
        log.error("Disciplina ja cadastrada", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.CONFLICT.value());
        errorDTO.setCodigoErro("DISCIPLINA_ALREADY_EXISTS");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(DisciplinaSemDocentesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleDisciplinaSemDocentesException(DisciplinaSemDocentesException ex,
                                                         HttpServletRequest request) {
        log.error("Disciplina sem docentes", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("DISCIPLINA_SEM_DOCENTES");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }

    @ExceptionHandler(DisciplinaDocenteNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorDTO handleDisciplinaDocenteNotFoundException(DisciplinaDocenteNotFoundException ex,
                                                             HttpServletRequest request) {
        log.error("Docente invalido para disciplina", ex);

        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMensagem(ex.getMessage());
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.BAD_REQUEST.value());
        errorDTO.setCodigoErro("DISCIPLINA_DOCENTE_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return errorDTO;
    }
}
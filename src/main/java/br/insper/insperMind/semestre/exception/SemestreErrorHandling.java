package br.insper.insperMind.semestre.exception;

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
public class SemestreErrorHandling {

    @ExceptionHandler(SemestreNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDTO handleSemestreNotFoundException(SemestreNotFoundException ex,
                                                 HttpServletRequest request) {

        log.error("Semestre nao encontrado");

        ErrorDTO errorDTO =  new ErrorDTO();
        errorDTO.setMensagem("Semestre nao encontrado");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.NOT_FOUND.value());
        errorDTO.setCodigoErro("CURSO_NOT_FOUND");
        errorDTO.setPath(request.getRequestURI());
        return  errorDTO;

    }

    @ExceptionHandler(SemestreAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDTO handleSemestreAlreadyExistsException(SemestreAlreadyExistsException ex,
                                                      HttpServletRequest request) {

        ErrorDTO errorDTO =  new ErrorDTO();
        errorDTO.setMensagem("Semestre já cadastrado");
        errorDTO.setData(LocalDateTime.now());
        errorDTO.setCodigoHttp(HttpStatus.CONFLICT.value());
        errorDTO.setCodigoErro("CURSO_ALREADY_EXISTS");
        errorDTO.setPath(request.getRequestURI());
        return  errorDTO;

    }


}

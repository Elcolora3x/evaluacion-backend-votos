package com.evaluacion.votos.evaluacion_backend_votos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CandidatoNoEncontradoException.class)
    public ResponseEntity<String> handleCandidatoNoEncontradoException(CandidatoNoEncontradoException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PartidoNoEncontradoException.class)
    public ResponseEntity<String> handlePartidoNoEncontradoException(PartidoNoEncontradoException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
}

package com.evaluacion.votos.evaluacion_backend_votos.exceptions;

public class CandidatoNoEncontradoException extends Exception{
    public CandidatoNoEncontradoException(Long id){
        super("El candidato con id " + id + " no se encuentra en la base de datos.");
    }
}

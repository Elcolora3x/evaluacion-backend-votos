package com.evaluacion.votos.evaluacion_backend_votos.exceptions;

public class PartidoNoEncontradoException extends Exception{
    public PartidoNoEncontradoException(Long id){
        super("El partido político con id " + id + " no se encuentra en la base de datos.");
    }
}

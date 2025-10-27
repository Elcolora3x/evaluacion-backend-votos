package com.evaluacion.votos.evaluacion_backend_votos.dtos;

import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;

public class CandidatoDTO {
    private Long id;
    private String nombre;
    private PartidoPoliticoDTO partidoPolitico;

    public CandidatoDTO() {
    }

    public CandidatoDTO(Long id, String nombre, PartidoPoliticoDTO partidoPolitico) {
        this.id = id;
        this.nombre = nombre;
        this.partidoPolitico = partidoPolitico;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public PartidoPoliticoDTO getPartidoPolitico() {
        return partidoPolitico;
    }
}

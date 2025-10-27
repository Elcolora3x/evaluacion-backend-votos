package com.evaluacion.votos.evaluacion_backend_votos.dtos;

public class PartidoPoliticoDTO {
    private Long id;
    private String nombre;
    private String sigla;

    public PartidoPoliticoDTO() {
    }

    public PartidoPoliticoDTO(Long id, String nombre, String sigla) {
        this.id = id;
        this.nombre = nombre;
        this.sigla = sigla;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getSigla() {
        return sigla;
    }

}

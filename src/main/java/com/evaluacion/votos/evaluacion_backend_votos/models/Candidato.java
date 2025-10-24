package com.evaluacion.votos.evaluacion_backend_votos.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name="candidato")
public class Candidato {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="nombre")
    private String nombre;

    @ManyToOne
    @JoinColumn(name="partido_id")
    private PartidoPolitico partidoPolitico;

    public Candidato() {
    }

    public Candidato(Long id, String nombre, PartidoPolitico partidoPolitico) {
        this.id = id;
        this.nombre = nombre;
        this.partidoPolitico = partidoPolitico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public PartidoPolitico getPartidoPolitico() {
        return partidoPolitico;
    }

    public void setPartidoPolitico(PartidoPolitico partidoPolitico) {
        this.partidoPolitico = partidoPolitico;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Candidato candidato = (Candidato) o;
        return Objects.equals(id, candidato.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

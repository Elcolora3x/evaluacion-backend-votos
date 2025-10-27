package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.PartidoPoliticoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class PartidoPoliticoService {
    private PartidoPoliticoRepository repository;

    @Autowired
    public PartidoPoliticoService(PartidoPoliticoRepository repository) {
        this.repository = repository;
    }

    public List<PartidoPoliticoDTO> getPartidos(){
        List<PartidoPolitico> partidos = repository.findAll();
        List<PartidoPoliticoDTO> partidosDTO = new ArrayList<>();

        for (PartidoPolitico partido : partidos) {
            PartidoPoliticoDTO partidoDTO = new PartidoPoliticoDTO(partido.getId(), partido.getNombre(), partido.getSigla());
            partidosDTO.add(partidoDTO);
        }

        return partidosDTO;
    }

    public PartidoPolitico createPartido(PartidoPolitico partidoPolitico){
        return repository.save(partidoPolitico);
    }

    public PartidoPolitico updatePartido(Long id, PartidoPolitico partidoActualizado) throws NoSuchElementException {
        Optional<PartidoPolitico> partido = repository.findById(id);

        if(partido.isPresent()){
            PartidoPolitico partidoExistente = partido.get();

            partidoExistente.setNombre(partidoActualizado.getNombre());
            partidoExistente.setSigla(partidoActualizado.getSigla());

           return repository.save(partidoExistente);
        }
        else{
            throw new NoSuchElementException("No se encontro el partido");
        }
    }

    public void deletePartido(Long id) throws NoSuchElementException {
        Optional<PartidoPolitico> partido = repository.findById(id);
        if(partido.isPresent()){
            repository.delete(partido.get());
        }
        else{
            throw new NoSuchElementException("No se encontro el partido");
        }
    }
}
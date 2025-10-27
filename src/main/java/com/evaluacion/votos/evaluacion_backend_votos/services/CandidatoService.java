package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CandidatoService {
    private CandidatoRepository repository;

    @Autowired
    public CandidatoService(CandidatoRepository candidatoRepository) {
        this.repository = candidatoRepository;
    }

    public List<CandidatoDTO> getCandidatos(){
        List<Candidato> candidatos = repository.findAll();
        List<CandidatoDTO> candidatosDTO = new ArrayList<>();

        for (Candidato candidato : candidatos) {
            CandidatoDTO candidatoDTO = new CandidatoDTO(candidato.getId(), candidato.getNombre(), candidato.getPartidoPolitico());
            candidatosDTO.add(candidatoDTO);
        }

        return candidatosDTO;
    }

    public static List<Candidato> getCandidatosByPartidoPolitico(PartidoPolitico partidoPolitico){
        return repository.findByPartidoPolitico(partidoPolitico);
    }

    public Candidato createCandidato(Candidato candidato){
        return repository.save(candidato);
    }

    public Candidato updateCandidato(Long id, Candidato candidatoActualizado) throws NoSuchElementException {
        Optional<Candidato> candidato = repository.findById(id);
        if(candidato.isPresent()){
            return repository.save(candidatoActualizado);
        }
        else{
            throw new NoSuchElementException("No se encontro el candidato");
        }
    }

    public void deleteCandidato(Long id) throws NoSuchElementException {
        Optional<Candidato> candidato = repository.findById(id);
        if(candidato.isPresent()){
            repository.delete(candidato.get());
        }
        else{
            throw new NoSuchElementException("No se encontro el candidato");
        }
    }
}

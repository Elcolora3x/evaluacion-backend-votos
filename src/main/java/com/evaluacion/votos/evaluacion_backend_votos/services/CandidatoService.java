package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.CandidatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidatoService {
    private CandidatoRepository repository;
    private PartidoPoliticoService partidoPoliticoService;

    @Autowired
    public CandidatoService(CandidatoRepository candidatoRepository,  PartidoPoliticoService partidoPoliticoService) {
        this.repository = candidatoRepository;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    public List<Candidato> init(List<Candidato> candidatos){
        return repository.saveAll(candidatos);
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

    public Candidato getCandidatoById(Long id)
            throws CandidatoNoEncontradoException {

        Optional<Candidato> candidato = repository.findById(id);
        if(candidato.isPresent()){
            return candidato.get();
        }
        else{
            throw new CandidatoNoEncontradoException(id);
        }
    }

    public List<Candidato> getCandidatosByPartidoPolitico(PartidoPolitico partidoPolitico){
        return repository.findByPartidoPolitico(partidoPolitico);
    }

    public Candidato createCandidato(Candidato candidato){
        return repository.save(candidato);
    }

    public Candidato updateCandidato(Long id, Candidato candidatoActualizado)
            throws CandidatoNoEncontradoException, PartidoNoEncontradoException {

        Optional<Candidato> candidato = repository.findById(id);

        if(candidato.isPresent()){
            PartidoPolitico partidoActualizado =  partidoPoliticoService.getPartidoById(candidatoActualizado.getPartidoPolitico().getId());
            Candidato candidatoExistente = candidato.get();

            candidatoExistente.setNombre(candidatoActualizado.getNombre());
            candidatoExistente.setPartidoPolitico(partidoActualizado);

            return repository.save(candidatoExistente);
        }
        else{
            throw new CandidatoNoEncontradoException(id);
        }
    }

    public void deleteCandidato(Long id)
            throws CandidatoNoEncontradoException {

        Optional<Candidato> candidato = repository.findById(id);
        if(candidato.isPresent()){
            repository.delete(candidato.get());
        }
        else{
            throw new CandidatoNoEncontradoException(id);
        }
    }
}

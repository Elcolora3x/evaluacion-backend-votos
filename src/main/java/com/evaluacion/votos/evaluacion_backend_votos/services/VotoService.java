package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VotoService {
    private VotoRepository repository;
    private CandidatoService candidatoService;


    @Autowired
    public VotoService(VotoRepository repository, CandidatoService candidatoService) {

        this.repository = repository;
        this.candidatoService = candidatoService;
    }

    public Voto createVoto(Voto voto) { return repository.save(voto); }

    public Integer getVotosByCandidato(Candidato candidato){
        List<Voto> votos = repository.findByCandidato(candidato);
        return votos.size();
    }

    public Integer getVotosByPartidoPolitico(PartidoPolitico partidoPolitico){
        Integer cantidadVotos = 0;
        List<Candidato> candidatosDelPartido = candidatoService.getCandidatosByPartidoPolitico(partidoPolitico);
        for (Candidato candidato : candidatosDelPartido) {
            cantidadVotos += getVotosByCandidato(candidato);
        }

        return cantidadVotos;
    }
}

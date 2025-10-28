package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.VotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VotoService {
    private VotoRepository repository;
    private CandidatoService candidatoService;
    private PartidoPoliticoService partidoPoliticoService;


    @Autowired
    public VotoService(VotoRepository repository, CandidatoService candidatoService, PartidoPoliticoService partidoPoliticoService) {

        this.repository = repository;
        this.candidatoService = candidatoService;
        this.partidoPoliticoService = partidoPoliticoService;
    }

    public List<Voto> init(List<Voto> votos){
        return repository.saveAll(votos);
    }

    public Voto createVoto(Voto voto) { return repository.save(voto); }

    public Integer getVotosByCandidato(Long candidatoId)
            throws CandidatoNoEncontradoException {

        Candidato candidato = candidatoService.getCandidatoById(candidatoId);

        List<Voto> votos = repository.findByCandidato(candidato);
        return votos.size();
    }

    public Integer getVotosByPartidoPolitico(Long partidoPoliticoId)
            throws PartidoNoEncontradoException,  CandidatoNoEncontradoException {

        PartidoPolitico partidoPolitico = partidoPoliticoService.getPartidoById(partidoPoliticoId);
        Integer cantidadVotos = 0;

        List<Candidato> candidatosDelPartido = candidatoService.getCandidatosByPartidoPolitico(partidoPolitico);
        for (Candidato candidato : candidatosDelPartido) {
            cantidadVotos += getVotosByCandidato(candidato.getId());
        }

        return cantidadVotos;
    }
}

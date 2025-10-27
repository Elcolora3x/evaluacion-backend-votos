package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.services.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votos")
public class VotoController {
    private VotoService service;

    @Autowired
    public VotoController(VotoService votoService) { this.service = votoService; }

    @GetMapping("/votosPorCandidato/{candidatoId}")
    public ResponseEntity<Integer> getVotosByCandidato(@PathVariable("{candidatoId}") Long candidatoId) {
        return new ResponseEntity<>(service.getVotosByCandidato(candidatoId), HttpStatus.OK);
    }

    @GetMapping("/votosPorPartido/{partidoId}")
    public ResponseEntity<Integer> getVotosByPartido(@PathVariable("{partidoId}") Long partidoId) {
        return new ResponseEntity<>(service.getVotosByPartidoPolitico(partidoId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Voto> createVoto(@RequestBody Voto voto){
        return new ResponseEntity<>(service.createVoto(voto), HttpStatus.CREATED);
    }
}

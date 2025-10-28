package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.services.CandidatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {
    private CandidatoService service;

    @Autowired
    public CandidatoController(CandidatoService service) { this.service = service; }


    @PostMapping("/init")
    public ResponseEntity<List<Candidato>> init(@RequestBody List<Candidato> candidatos) {
        return new ResponseEntity<>(service.init(candidatos), HttpStatus.CREATED);
    }


    @GetMapping
    public ResponseEntity<List<CandidatoDTO>> getCandidatos(){
        return new ResponseEntity<>(service.getCandidatos(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Candidato> createCandidato(@RequestBody Candidato candidato){
        return new  ResponseEntity<>(service.createCandidato(candidato), HttpStatus.CREATED);
    }

    @PutMapping("/{candidatoId}")
    public ResponseEntity<Candidato> updateCandidato(@PathVariable("candidatoId") Long candidatoId, @RequestBody Candidato candidato)
            throws CandidatoNoEncontradoException {

        return new ResponseEntity<>(service.updateCandidato(candidatoId, candidato), HttpStatus.OK);
    }

    @DeleteMapping("/{candidatoId}")
    public ResponseEntity<Candidato> deleteCandidato(@PathVariable("candidatoId") Long candidatoId)
            throws CandidatoNoEncontradoException {

        service.deleteCandidato(candidatoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


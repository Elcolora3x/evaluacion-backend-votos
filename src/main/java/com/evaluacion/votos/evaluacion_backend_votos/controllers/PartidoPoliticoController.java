package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.services.PartidoPoliticoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/partidos")
public class PartidoPoliticoController {
    private PartidoPoliticoService service;

    @Autowired
    PartidoPoliticoController(PartidoPoliticoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<PartidoPoliticoDTO>> getPartidos(){
        return new ResponseEntity<>(service.getPartidos(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PartidoPolitico> createPartido(@RequestBody PartidoPolitico partidoNuevo){
        return new ResponseEntity<>(service.createPartido(partidoNuevo), HttpStatus.CREATED);
    }

    @PutMapping("/{partidoId}")
    public ResponseEntity<PartidoPolitico> updatePartido(@PathVariable("partidoId") Long partidoId, @RequestBody PartidoPolitico partidoNuevo) throws NoSuchElementException {
        return new ResponseEntity<>(service.updatePartido(partidoId, partidoNuevo), HttpStatus.OK);
    }

    @DeleteMapping("/{partidoId}")
    public ResponseEntity<Void> deletePartido(@PathVariable("partidoId") Long partidoId){
        service.deletePartido(partidoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

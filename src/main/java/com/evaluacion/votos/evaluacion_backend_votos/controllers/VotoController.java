package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.services.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/votos")
public class VotoController {
    private VotoService service;

    @Autowired
    public VotoController(VotoService votoService) { this.service = votoService; }


    @PostMapping("/init")
    @Operation(
            summary = "Método de inicialización de datos. Debe ejecutarse primero para poblar la base de datos!!",
            description = "Recibe una lista de objetos tipo Voto y los inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instancias creadas exitosamente.")
    })
    public ResponseEntity<List<Voto>> init(@RequestBody List<Voto> votos) {
        return new ResponseEntity<>(service.init(votos), HttpStatus.CREATED);
    }


    @GetMapping("/votosPorCandidato/{candidatoId}")
    @Operation(
            summary = "Método de obtención de la cantidad de votos de un candidato",
            description = "Devuelve un número entero que representa los votos totales del candidato cuyo id se envía en la request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad de votos del candidato contada exitosamente."),
            @ApiResponse(responseCode = "404", description = "El candidato con el id enviado no se encuentra en la base de datos.")
    })
    public ResponseEntity<Integer> getVotosByCandidato(@PathVariable("candidatoId") Long candidatoId)
            throws CandidatoNoEncontradoException {

        return new ResponseEntity<>(service.getVotosByCandidato(candidatoId), HttpStatus.OK);
    }

    @GetMapping("/votosPorPartido/{partidoId}")
    @Operation(
            summary = "Método de obtención de la cantidad de votos de un partido político",
            description = "Devuelve un número entero que representa los votos totales del partido político cuyo id se envía en la request."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cantidad de votos del partido contada exitosamente."),
            @ApiResponse(responseCode = "404", description = "El candidato con el id enviado no se encuentra en la base de datos."),
            @ApiResponse(responseCode = "404", description = "El partido con el id enviado no se encuentra en la base de datos.")
    })
    public ResponseEntity<Integer> getVotosByPartido(@PathVariable("partidoId") Long partidoId)
            throws PartidoNoEncontradoException, CandidatoNoEncontradoException {

        return new ResponseEntity<>(service.getVotosByPartidoPolitico(partidoId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "Método de creación de un voto",
            description = "Recibe un objeto del tipo Voto y lo inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Voto creado exitosamente."),
            @ApiResponse(responseCode = "404", description = "El candidato con el id enviado no se encuentra en la base de datos.")
    })
    public ResponseEntity<Voto> createVoto(@RequestBody Voto voto)
            throws CandidatoNoEncontradoException {

        return new ResponseEntity<>(service.createVoto(voto), HttpStatus.CREATED);
    }
}

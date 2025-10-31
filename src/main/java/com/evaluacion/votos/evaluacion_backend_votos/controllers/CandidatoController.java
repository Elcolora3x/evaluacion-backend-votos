package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.services.CandidatoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Método de inicialización de datos. Debe ejecutarse primero para poblar la base de datos!!",
            description = "Recibe una lista de objetos tipo Candidato y los inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instancias creadas exitosamente.")
    })
    public ResponseEntity<List<Candidato>> init(@RequestBody List<Candidato> candidatos) {
        return new ResponseEntity<>(service.init(candidatos), HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(
            summary = "Metodo de obtención de los partidos candidatos existentes.",
            description = "Devuelve una lista de objetos tipo Candidato mostrando todos los candidatos de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidatos mostrados exitosamente.")
    })
    public ResponseEntity<List<CandidatoDTO>> getCandidatos(){
        return new ResponseEntity<>(service.getCandidatos(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "Método de creación de un candidato",
            description = "Recibe un objeto del tipo Candidato y lo inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato creado exitosamente.")
    })
    public ResponseEntity<Candidato> createCandidato(@RequestBody Candidato candidato){
        return new  ResponseEntity<>(service.createCandidato(candidato), HttpStatus.CREATED);
    }

    @PutMapping("/{candidatoId}")
    @Operation(
            summary = "Método de actualización de atributos de un candidato",
            description = "Recibe el atributo id del Candidato a actualizar y un objeto del tipo Candidato con los datos nuevos y actualiza la fila correspondiente en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Candidato actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "El candidato con el id enviado no se encuentra en la base de datos."),
            @ApiResponse(responseCode = "404", description = "El partido con el id enviado no se encuentra en la base de datos.")
    })
    public ResponseEntity<Candidato> updateCandidato(@PathVariable("candidatoId") Long candidatoId, @RequestBody Candidato candidato)
            throws CandidatoNoEncontradoException, PartidoNoEncontradoException {

        return new ResponseEntity<>(service.updateCandidato(candidatoId, candidato), HttpStatus.OK);
    }

    @DeleteMapping("/{candidatoId}")
    @Operation(
            summary = "Método de eliminación de un candidato",
            description = "Recibe el atributo id del Candidato a eliminar y lo quita de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Candidato eliminado exitosamente.")
    })
    public ResponseEntity<Candidato> deleteCandidato(@PathVariable("candidatoId") Long candidatoId)
            throws CandidatoNoEncontradoException {

        service.deleteCandidato(candidatoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


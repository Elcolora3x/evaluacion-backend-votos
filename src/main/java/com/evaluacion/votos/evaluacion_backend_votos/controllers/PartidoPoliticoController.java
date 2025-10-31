package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.services.PartidoPoliticoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partidos")
public class PartidoPoliticoController {
    private PartidoPoliticoService service;

    @Autowired
    PartidoPoliticoController(PartidoPoliticoService service) {
        this.service = service;
    }


    @PostMapping("/init")
    @Operation(
            summary = "Método de inicialización de datos. Debe ejecutarse primero para poblar la base de datos!!",
            description = "Recibe una lista de objetos tipo PartidoPolitico y los inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instancias creadas exitosamente.")
    })
    public ResponseEntity<List<PartidoPolitico>> init(@RequestBody List<PartidoPolitico> partidosPoliticos) {
        return new ResponseEntity<>(service.init(partidosPoliticos), HttpStatus.CREATED);
    }


    @GetMapping
    @Operation(
            summary = "Metodo de obtención de los partidos políticos existentes.",
            description = "Devuelve una lista de objetos tipo PartidoPolitico mostrando todos los partidos de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partidos mostrados exitosamente")
    })
    public ResponseEntity<List<PartidoPoliticoDTO>> getPartidos(){
        return new ResponseEntity<>(service.getPartidos(), HttpStatus.OK);
    }

    @PostMapping
    @Operation(
            summary = "Método de creación de un partido político",
            description = "Recibe un objeto del tipo PartidoPolitico y lo inserta en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Instancias creadas exitosamente.")
    })
    public ResponseEntity<PartidoPolitico> createPartido(@RequestBody PartidoPolitico partidoNuevo){
        return new ResponseEntity<>(service.createPartido(partidoNuevo), HttpStatus.CREATED);
    }

    @PutMapping("/{partidoId}")
    @Operation(
            summary = "Método de actualización de atributos de un partido político",
            description = "Recibe el atributo id del PartidoPolitico a actualizar y un objeto del tipo PartidoPolitico con los datos nuevos y actualiza la fila correspondiente en la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Partido actualizado exitosamente."),
            @ApiResponse(responseCode = "404", description = "El partido con el id enviado no se encuentra en la base de datos.")
    })
    public ResponseEntity<PartidoPolitico> updatePartido(@PathVariable("partidoId") Long partidoId, @RequestBody PartidoPolitico partidoNuevo)
            throws PartidoNoEncontradoException {

        return new ResponseEntity<>(service.updatePartido(partidoId, partidoNuevo), HttpStatus.OK);
    }

    @DeleteMapping("/{partidoId}")
    @Operation(
            summary = "Método de eliminación de un partido político",
            description = "Recibe el atributo id del PartidoPolitico a eliminar y lo quita de la base de datos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Partido eliminado exitosamente.")
    })
    public ResponseEntity<Void> deletePartido(@PathVariable("partidoId") Long partidoId)
            throws PartidoNoEncontradoException {

        service.deletePartido(partidoId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

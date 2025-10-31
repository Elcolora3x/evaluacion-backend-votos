package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.services.CandidatoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CandidatoController.class)
class CandidatoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CandidatoService service;

    private PartidoPolitico partido1;
    private Candidato candidato1;
    private CandidatoDTO candidatoDTO1;

    @BeforeEach
    void setUp() {

        partido1 = new PartidoPolitico();
        partido1.setId(1L);
        partido1.setNombre("Partido1");
        partido1.setSigla("P1");

        candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setNombre("Candidato1");
        candidato1.setPartidoPolitico(partido1);

        candidatoDTO1 = new CandidatoDTO(1L, "Candidato1", partido1);
    }

    @Test
    @DisplayName("Test de cargado de datos")
    void testInitSuccess() throws Exception {

        List<Candidato> candidatos = Collections.singletonList(candidato1);

        when(service.init(any(List.class))).thenReturn(candidatos);

        mockMvc.perform(post("/api/candidatos/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatos)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(candidato1.getNombre())));
    }

    @Test
    @DisplayName("test de busqueda de candidatos")
    void testGetCandidatosSuccess() throws Exception {

        List<CandidatoDTO> dtoList = Collections.singletonList(candidatoDTO1);

        when(service.getCandidatos()).thenReturn(dtoList);

        mockMvc.perform(get("/api/candidatos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is(candidatoDTO1.getNombre())))
                .andExpect(jsonPath("$[0].partidoPolitico.nombre", is("Partido1")));
    }

    @Test
    @DisplayName("test de creacion de candidato")
    void testCreateCandidatoSuccess() throws Exception {

        when(service.createCandidato(any(Candidato.class))).thenReturn(candidato1);

        mockMvc.perform(post("/api/candidatos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidato1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is(candidato1.getNombre())));
    }

    @Test
    @DisplayName("test de actualizacion de candidato")
    void testUpdateCandidatoSuccess() throws Exception {

        Long candidatoId = 1L;
        Candidato candidatoActualizado = new Candidato(candidatoId, "Candidato Actualizado", partido1);

        when(service.updateCandidato(eq(candidatoId), any(Candidato.class))).thenReturn(candidatoActualizado);

        mockMvc.perform(put("/api/candidatos/{candidatoId}", candidatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidatoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Candidato Actualizado")));
    }

    @Test
    @DisplayName("test de excepcion CandidatoNoEncontradoException en metodo UpdateCandidato")
    void testUpdateCandidatoNotFound1() throws Exception {

        Long candidatoIdInexistente = 99L;

        when(service.updateCandidato(eq(candidatoIdInexistente), any(Candidato.class)))
                .thenThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        mockMvc.perform(put("/api/candidatos/{candidatoId}", candidatoIdInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidato1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("test de excepcion PartidoNoEncontradoException en metodo UpdateCandidato")
    void testUpdateCandidatoNotFound2() throws Exception {

        Long candidatoId = 1L;
        Long partidoIdNoExistente = 99L;

        when(service.updateCandidato(eq(candidatoId), any(Candidato.class)))
                .thenThrow(new PartidoNoEncontradoException(partidoIdNoExistente));

        mockMvc.perform(put("/api/candidatos/{candidatoId}", candidatoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(candidato1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("test de eliminacion de candidato")
    void testDeleteCandidatoSuccess() throws Exception {

        Long candidatoId = 1L;

        doNothing().when(service).deleteCandidato(candidatoId);

        mockMvc.perform(delete("/api/candidatos/{candidatoId}", candidatoId))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deleteCandidato(candidatoId);
    }

    @Test
    @DisplayName("test de metodo CandidatoNoEncontradoException en metodo DeleteCandidato")
    void testDeleteCandidatoNotFound() throws Exception {

        Long candidatoIdInexistente = 99L;

        doThrow(new CandidatoNoEncontradoException(candidatoIdInexistente)).when(service).deleteCandidato(candidatoIdInexistente);

        mockMvc.perform(delete("/api/candidatos/{candidatoId}", candidatoIdInexistente))
                .andExpect(status().isNotFound());
    }
}
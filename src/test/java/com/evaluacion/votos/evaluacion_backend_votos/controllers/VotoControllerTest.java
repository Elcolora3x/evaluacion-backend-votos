package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.services.VotoService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VotoController.class)
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VotoService service;

    private Voto voto1;
    private Candidato candidato1;
    private PartidoPolitico partido1;

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

        voto1 = new Voto();
        voto1.setId(1L);
        voto1.setCandidato(candidato1);
    }

    @Test
    @DisplayName("test de cargado de datos")
    void testInitSuccess() throws Exception {

        List<Voto> votos = Collections.singletonList(voto1);

        when(service.init(any(List.class))).thenReturn(votos);

        mockMvc.perform(post("/api/votos/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(votos)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("Test de conteo de votos de un candidato")
    void testGetVotosByCandidatoSuccess() throws Exception {

        Long candidatoId = 1L;
        Integer totalVotos = 15;

        when(service.getVotosByCandidato(candidatoId)).thenReturn(totalVotos);

        mockMvc.perform(get("/api/votos/votosPorCandidato/{candidatoId}", candidatoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("15"));
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException para metodo GetVotosByCandidato")
    void testGetVotosByCandidatoNotFound() throws Exception {

        Long candidatoIdInexistente = 99L;

        when(service.getVotosByCandidato(candidatoIdInexistente))
                .thenThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        mockMvc.perform(get("/api/votos/votosPorCandidato/{candidatoId}", candidatoIdInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test de conteo de votos de un partido politico")
    void testGetVotosByPartidoPoliticoSuccess() throws Exception {

        Long partidoId = 1L;
        Integer totalVotosPartido = 15;

        when(service.getVotosByPartidoPolitico(partidoId)).thenReturn(totalVotosPartido);

        mockMvc.perform(get("/api/votos/votosPorPartido/{partidoId}", partidoId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("15"));
    }

    @Test
    @DisplayName("Test de excepcion PartidoNoEncontradoException en metodo GetVotosByPartidoPolitico")
    void testGetVotosByPartidoPoliticoNotFound1() throws Exception {

        Long partidoIdInexistente = 99L;

        when(service.getVotosByPartidoPolitico(partidoIdInexistente))
                .thenThrow(new PartidoNoEncontradoException(partidoIdInexistente));

        mockMvc.perform(get("/api/votos/votosPorPartido/{partidoId}", partidoIdInexistente))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException en metodo GetVotosByPartidoPolitico")
    void testGetVotosByPartidoPoliticoCandidatoNotFound2() throws Exception {

        Long partidoId = 1L;
        Long candidatoIdInexistente = 99L;

        when(service.getVotosByPartidoPolitico(partidoId))
                .thenThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        mockMvc.perform(get("/api/votos/votosPorPartido/{partidoId}", partidoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test de creacion de voto")
    void testCreateVotoSuccess() throws Exception {

        when(service.createVoto(any(Voto.class))).thenReturn(voto1);

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voto1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.candidato.id", is(1)));
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException para metodo CreateVoto")
    void testCreateVotoNotFound() throws Exception {

        Long candidatoIdInexistente = 99L;

        when(service.createVoto(any(Voto.class)))
                .thenThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        mockMvc.perform(post("/api/votos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(voto1)))
                .andExpect(status().isNotFound());
    }
}
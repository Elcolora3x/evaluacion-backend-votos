package com.evaluacion.votos.evaluacion_backend_votos.controllers;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.services.PartidoPoliticoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
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

@WebMvcTest(PartidoPoliticoController.class)
class PartidoPoliticoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartidoPoliticoService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PartidoPolitico partido1;
    private PartidoPoliticoDTO partidoDTO1;

    @BeforeEach
    void setUp() {

        partido1 = new PartidoPolitico();
        partido1.setId(1L);
        partido1.setNombre("Partido1");
        partido1.setSigla("P1");

        partidoDTO1 = new PartidoPoliticoDTO(1L, "Partido1", "P1");
    }

    @Test
    @DisplayName("test de cargado de datos")
    void testInitSuccess() throws Exception {

        List<PartidoPolitico> partidos = Collections.singletonList(partido1);

        when(service.init(any(List.class))).thenReturn(partidos);

        mockMvc.perform(post("/api/partidos/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partidos)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nombre", is(partido1.getNombre())));
    }

    @Test
    @DisplayName("test de busqueda de partidos")
    void testGetPartidosSuccess() throws Exception {

        List<PartidoPoliticoDTO> dtoList = Collections.singletonList(partidoDTO1);

        when(service.getPartidos()).thenReturn(dtoList);

        mockMvc.perform(get("/api/partidos"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is(partidoDTO1.getNombre())));
    }

    @Test
    @DisplayName("test de creacion de partido")
    void testCreatePartidoSuccess() throws Exception {

        when(service.createPartido(any(PartidoPolitico.class))).thenReturn(partido1);

        mockMvc.perform(post("/api/partidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partido1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is(partido1.getNombre())));
    }

    @Test
    @DisplayName("test de actualizacion de partido")
    void testUpdatePartidoSuccess() throws Exception {

        Long partidoId = 1L;
        PartidoPolitico partidoActualizado = new PartidoPolitico(partidoId, "Nombre Actualizado","NA" );

        when(service.updatePartido(eq(partidoId), any(PartidoPolitico.class))).thenReturn(partidoActualizado);

        mockMvc.perform(put("/api/partidos/{partidoId}", partidoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partidoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Nombre Actualizado")))
                .andExpect(jsonPath("$.sigla", is("NA")));
    }

    @Test
    @DisplayName("text de excepcion PartidoNoEncontradoException en metodo UpdatePartido")
    void testUpdatePartidoNotFound() throws Exception {

        Long partidoIdInexistente = 99L;

        when(service.updatePartido(eq(partidoIdInexistente), any(PartidoPolitico.class)))
                .thenThrow(new PartidoNoEncontradoException(partidoIdInexistente));

        mockMvc.perform(put("/api/partidos/{partidoId}", partidoIdInexistente)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partido1)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Test de eliminacion de partido")
    void testDeletePartidoSuccess() throws Exception {

        Long partidoId = 1L;

        doNothing().when(service).deletePartido(partidoId);

        mockMvc.perform(delete("/api/partidos/{partidoId}", partidoId))
                .andExpect(status().isNoContent());

        verify(service, times(1)).deletePartido(partidoId);
    }

    @Test
    @DisplayName("test de excepcion PartidoNoEncontradoException en metodo DeletePartido")
    void testDeletePartidoNotFound() throws Exception {

        Long partidoIdInexistente = 99L;

        doThrow(new PartidoNoEncontradoException(partidoIdInexistente)).when(service).deletePartido(partidoIdInexistente);

        mockMvc.perform(delete("/api/partidos/{partidoId}", partidoIdInexistente))
                .andExpect(status().isNotFound());
    }
}
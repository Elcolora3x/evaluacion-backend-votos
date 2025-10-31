package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.VotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private CandidatoService candidatoService;

    @Mock
    private PartidoPoliticoService partidoPoliticoService;

    @InjectMocks
    private VotoService votoService;

    private PartidoPolitico partido;
    private Candidato candidato1;
    private Candidato candidato2;
    private Voto voto1;
    private Voto voto2;
    private Voto voto3;

    @BeforeEach
    void setUp() {
        partido = new PartidoPolitico();
        partido.setId(1L);
        partido.setNombre("Partido 1");

        candidato1 = new Candidato();
        candidato1.setId(1L);
        candidato1.setNombre("Candidato 1");
        candidato1.setPartidoPolitico(partido);

        candidato2 = new Candidato();
        candidato2.setId(2L);
        candidato2.setNombre("Candidato 2");
        candidato2.setPartidoPolitico(partido);

        voto1 = new Voto();
        voto1.setId(1L);
        voto1.setCandidato(candidato1);

        voto2 = new Voto();
        voto2.setId(2L);
        voto2.setCandidato(candidato1);

        voto3 = new Voto();
        voto3.setId(3L);
        voto3.setCandidato(candidato2);
    }

    @Test
    @DisplayName("Test de cargado de datos")
    void testInitSuccess() {
        List<Voto> listaVotos = List.of(voto1, voto2);
        given(votoRepository.saveAll(listaVotos)).willReturn(listaVotos);

        List<Voto> resultado = votoService.init(listaVotos);

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(votoRepository, times(1)).saveAll(listaVotos);
    }

    @Test
    @DisplayName("Test de creacion de voto")
    void testCreateVotoSuccess()
            throws CandidatoNoEncontradoException {

        given(votoRepository.save(any(Voto.class))).willReturn(voto1);

        Voto resultado = votoService.createVoto(voto1);

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(1L, resultado.getId())
        );

        verify(votoRepository, times(1)).save(voto1);
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException para metodo CreateVoto")
    void testCreateVotoNotFound()
            throws CandidatoNoEncontradoException {

        Long candidatoIdInexistente = 99L;
        String mensajeExcepcion = "El candidato con id 99 no se encuentra en la base de datos.";

        given(candidatoService.getCandidatoById(candidatoIdInexistente))
                .willThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        CandidatoNoEncontradoException excepcion = assertThrows(CandidatoNoEncontradoException.class,
                () -> votoService.getVotosByCandidato(candidatoIdInexistente)
        );

        assertEquals(mensajeExcepcion, excepcion.getMessage());
        verify(votoRepository, never()).save(any(Voto.class));
    }

    @Test
    @DisplayName("Test de conteo de votos de un candidato")
    void testGetVotosByCandidatoSuccess()
            throws CandidatoNoEncontradoException {

        Long candidatoId = 1L;
        List<Voto> votosDelCandidato = List.of(voto1, voto2);

        given(candidatoService.getCandidatoById(candidatoId)).willReturn(candidato1);
        given(votoRepository.findByCandidato(candidato1)).willReturn(votosDelCandidato);

        Integer votosTotales = votoService.getVotosByCandidato(candidatoId);

        assertEquals(2, votosTotales);
        verify(candidatoService, times(1)).getCandidatoById(candidatoId);
        verify(votoRepository, times(1)).findByCandidato(candidato1);
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException para metodo GetVotosByCandidato")
    void testGetVotosByCandidatoNotFound()
            throws CandidatoNoEncontradoException {

        Long candidatoIdInexistente = 99L;
        String mensajeExcepcion = "El candidato con id 99 no se encuentra en la base de datos.";

        given(candidatoService.getCandidatoById(candidatoIdInexistente))
                .willThrow(new CandidatoNoEncontradoException(candidatoIdInexistente));

        CandidatoNoEncontradoException exception = assertThrows(
                CandidatoNoEncontradoException.class,
                () -> votoService.getVotosByCandidato(candidatoIdInexistente)
        );

        assertEquals(mensajeExcepcion, exception.getMessage());
        verify(votoRepository, never()).findByCandidato(any());
    }

    @Test
    @DisplayName("Test de conteo de votos de un partido politico")
    void testGetVotosByPartidoPoliticoSuccess()
            throws PartidoNoEncontradoException, CandidatoNoEncontradoException {

        Long partidoId = 1L;
        Long candidato1Id = 1L;
        Long candidato2Id = 2L;

        List<Candidato> candidatosDelPartido = List.of(candidato1, candidato2);

        List<Voto> votosCandidato1 = List.of(voto1, voto2);
        List<Voto> votosCandidato2 = List.of(voto3);

        given(partidoPoliticoService.getPartidoById(partidoId)).willReturn(partido);
        given(candidatoService.getCandidatosByPartidoPolitico(partido)).willReturn(candidatosDelPartido);

        given(candidatoService.getCandidatoById(candidato1Id)).willReturn(candidato1);
        given(votoRepository.findByCandidato(candidato1)).willReturn(votosCandidato1);

        given(candidatoService.getCandidatoById(candidato2Id)).willReturn(candidato2);
        given(votoRepository.findByCandidato(candidato2)).willReturn(votosCandidato2);

        Integer votosTotales = votoService.getVotosByPartidoPolitico(partidoId);

        assertEquals(3, votosTotales);

        verify(partidoPoliticoService, times(1)).getPartidoById(partidoId);
        verify(candidatoService, times(1)).getCandidatosByPartidoPolitico(partido);
        verify(votoRepository, times(1)).findByCandidato(candidato1);
        verify(votoRepository, times(1)).findByCandidato(candidato2);
    }

    @Test
    @DisplayName("Test de excepcion PartidoNoEncontradoException en metodo GetVotosByPartidoPolitico")
    void testGetVotosByPartidoPoliticoNotFound()
            throws PartidoNoEncontradoException {

        Long partidoId = 99L;
        String mensajeExcepcion = "El partido político con id 99 no se encuentra en la base de datos.";

        given(partidoPoliticoService.getPartidoById(partidoId))
                .willThrow(new PartidoNoEncontradoException(partidoId));

        PartidoNoEncontradoException exception = assertThrows(
                PartidoNoEncontradoException.class,
                () -> votoService.getVotosByPartidoPolitico(partidoId)
        );

        assertEquals(mensajeExcepcion, exception.getMessage());

        verify(candidatoService, never()).getCandidatosByPartidoPolitico(any());
        verify(votoRepository, never()).findByCandidato(any());
    }
}
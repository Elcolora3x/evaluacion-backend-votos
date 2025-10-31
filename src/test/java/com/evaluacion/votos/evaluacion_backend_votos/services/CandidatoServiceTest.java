package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.CandidatoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.CandidatoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.CandidatoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CandidatoServiceTest {

    @Mock
    private CandidatoRepository candidatoRepository;

    @Mock
    private PartidoPoliticoService partidoPoliticoService;

    @InjectMocks
    private CandidatoService candidatoService;

    private PartidoPolitico partido1;
    private PartidoPolitico partido2;
    private Candidato candidato1;
    private Candidato candidato2;

    @BeforeEach
    void setUp() {
        partido1 = new PartidoPolitico();
        partido1.setId(1L);
        partido1.setNombre("Partido 1");

        partido2 = new PartidoPolitico();
        partido2.setId(2L);
        partido2.setNombre("Partido 2");

        candidato1 = new Candidato();
        candidato1.setNombre("Candidato 1");
        candidato1.setPartidoPolitico(partido1);

        candidato2 = new Candidato();
        candidato2.setNombre("Candidato 2");
        candidato2.setPartidoPolitico(partido1);
    }

    @Test
    @DisplayName("Test de cargado de datos")
    void testInitSuccess() {

        List<Candidato> listaCandidatos = List.of(candidato1, candidato2);
        given(candidatoRepository.saveAll(listaCandidatos)).willReturn(listaCandidatos);

        List<Candidato> resultado = candidatoService.init(listaCandidatos);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(candidatoRepository, times(1)).saveAll(listaCandidatos);
    }

    @Test
    @DisplayName("Test de busqueda de candidatos")
    void testGetCandidatosSuccess(){

        List<Candidato> listaCandidatos = List.of(candidato1);

        given(candidatoRepository.findAll()).willReturn(listaCandidatos);

        candidatoRepository.saveAll(listaCandidatos);
        List<CandidatoDTO> resultado = candidatoService.getCandidatos();

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size())
        );

        verify(candidatoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test de busqueda de candidato por id")
    void testGetCandidatoByIdSuccess()
            throws CandidatoNoEncontradoException {

        Long idCandidato = 1L;

        given(candidatoRepository.findById(idCandidato)).willReturn(Optional.ofNullable(candidato1));

        Candidato resultado = candidatoService.getCandidatoById(idCandidato);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(candidato1, resultado)
        );

        verify(candidatoRepository, times(1)).findById(idCandidato);
    }

    @Test
    @DisplayName("Test de exepcion CandidatoNoEncontradoException en metodo GetCandidatoById")
    void testGetCandidatoByIdNotFound()
            throws CandidatoNoEncontradoException {

        Long idCandidatoInexistente = 99L;
        String mensajeExcepcion = "El candidato con id 99 no se encuentra en la base de datos.";

        given(candidatoRepository.findById(idCandidatoInexistente)).willReturn(Optional.empty());

        CandidatoNoEncontradoException excepcion = assertThrows(
                CandidatoNoEncontradoException.class,
                () -> candidatoService.getCandidatoById(idCandidatoInexistente)
        );

        assertEquals(mensajeExcepcion, excepcion.getMessage());
    }

    @Test
    @DisplayName("Test de busqueda de candidatos por partido politico")
    void testGetCandidatoByPartidoPoliticoSuccess(){
        List<Candidato> listaCandidatos = List.of(candidato1,  candidato2);

        given(candidatoRepository.findByPartidoPolitico(partido1)).willReturn(listaCandidatos);

        List<Candidato> resultado = candidatoService.getCandidatosByPartidoPolitico(partido1);

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(candidatoRepository, times(1)).findByPartidoPolitico(partido1);
    }

    @Test
    @DisplayName("Test de creacion de candidato")
    void testCreateCandidatoSuccess(){

        given(candidatoRepository.save(candidato1)).willReturn(candidato1);

        Candidato resultado =  candidatoService.createCandidato(candidato1);

        assertAll(
                () -> assertNotNull(resultado),
                () -> assertEquals(candidato1, resultado)
        );

        verify(candidatoRepository, times(1)).save(candidato1);
    }

    @Test
    @DisplayName("Test de actualizacion de candidato")
    void testUpdateCandidatoSuccess()
            throws CandidatoNoEncontradoException, PartidoNoEncontradoException {

        Long idCandidato = 1L;

        Candidato candidatoActualizado = new Candidato(idCandidato, "Candidato Actualizado", partido2);

        given(candidatoRepository.findById(idCandidato)).willReturn(Optional.ofNullable(candidato1));
        given(partidoPoliticoService.getPartidoById(partido2.getId())).willReturn(partido2);

        given(candidatoRepository.save(candidato1)).willReturn(candidato1);

        Candidato resultado = candidatoService.updateCandidato(idCandidato, candidatoActualizado);

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(candidatoActualizado.getNombre(), resultado.getNombre()),
                () -> assertEquals(partido2, resultado.getPartidoPolitico())
        );

        verify(candidatoRepository, times(1)).findById(idCandidato);
        verify(partidoPoliticoService, times(1)).getPartidoById(partido2.getId());
        verify(candidatoRepository, times(1)).save(candidato1);
    }

    @Test
    @DisplayName("Test de excepcion CandidatoNoEncontradoException en metodo UpdateCandidato")
    void testUpdateCandidatoNotFoundException1()
            throws PartidoNoEncontradoException {

        Long candidatoIdInexistente = 99L;
        String mensajeExcepcion = "El candidato con id 99 no se encuentra en la base de datos.";

        Candidato candidatoActualizado = new Candidato(candidatoIdInexistente, "Candidato Fantasma", partido2);

        given(candidatoRepository.findById(candidatoIdInexistente)).willReturn(Optional.empty());

        CandidatoNoEncontradoException excepcion = assertThrows(
                CandidatoNoEncontradoException.class,
                () -> candidatoService.updateCandidato(candidatoIdInexistente, candidatoActualizado)

        );

        assertEquals(mensajeExcepcion, excepcion.getMessage());

        verify(candidatoRepository, times(1)).findById(candidatoIdInexistente);

        verify(candidatoRepository, never()).save(any(Candidato.class));
        verify(partidoPoliticoService, never()).getPartidoById(anyLong());
    }

    @Test
    @DisplayName("Test de excepcion PartidoNoEncontradoException en metodo UpdateCandidato")
    void testUpdateCandidatoNotFoundException2()
            throws PartidoNoEncontradoException {

        Long partidoIdInexistente = 99L;
        String mensajeExcepcion = "El partido político con id 99 no se encuentra en la base de datos.";

        PartidoPolitico partidoFantasma = new PartidoPolitico(partidoIdInexistente, "Partido Fantasma", "PF");

        Candidato candidatoActualizado = new Candidato(candidato1.getId(), "Candidato Actualizado", partidoFantasma);

        given(candidatoRepository.findById(candidato1.getId())).willReturn(Optional.of(candidato1));

        given(partidoPoliticoService.getPartidoById(partidoIdInexistente))
                .willThrow(new PartidoNoEncontradoException(partidoIdInexistente));

        PartidoNoEncontradoException excepcion = assertThrows(
                PartidoNoEncontradoException.class,
                () -> candidatoService.updateCandidato(candidato1.getId(), candidatoActualizado)
        );

        assertEquals(mensajeExcepcion, excepcion.getMessage());

        verify(candidatoRepository, times(1)).findById(candidato1.getId());
        verify(partidoPoliticoService, times(1)).getPartidoById(partidoIdInexistente);
        verify(candidatoRepository, never()).save(any(Candidato.class));
    }

    @Test
    @DisplayName("Test de eliminacion de candidato")
    void testDeleteCandidatoSuccess()
            throws CandidatoNoEncontradoException {

        Long idCandidato = 1L;

        given(candidatoRepository.findById(idCandidato)).willReturn(Optional.ofNullable(candidato1));

        candidatoService.deleteCandidato(idCandidato);

        verify(candidatoRepository, times(1)).findById(idCandidato);
        verify(candidatoRepository, times(1)).delete(candidato1);
    }

    @Test
    @DisplayName("Test de excepcion PartidoNoEncontradoException para metodo DeleteCandidato")
    void testDeleteCandidatoNotFound() {

        Long idCandidatoInexistente = 99L;
        String mensajeExcepcion = "El candidato con id 99 no se encuentra en la base de datos.";

        given(candidatoRepository.findById(idCandidatoInexistente)).willReturn(Optional.empty());

            CandidatoNoEncontradoException excepcion = assertThrows(
                CandidatoNoEncontradoException.class,
                () -> candidatoService.deleteCandidato(idCandidatoInexistente)
        );

        assertEquals(mensajeExcepcion, excepcion.getMessage());
    }
}
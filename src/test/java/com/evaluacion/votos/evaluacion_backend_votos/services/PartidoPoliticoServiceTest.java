package com.evaluacion.votos.evaluacion_backend_votos.services;

import com.evaluacion.votos.evaluacion_backend_votos.dtos.PartidoPoliticoDTO;
import com.evaluacion.votos.evaluacion_backend_votos.exceptions.PartidoNoEncontradoException;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import com.evaluacion.votos.evaluacion_backend_votos.repositories.PartidoPoliticoRepository;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PartidoPoliticoServiceTest {

    @Mock
    private PartidoPoliticoRepository partidoPoliticoRepository;

    @InjectMocks
    private PartidoPoliticoService partidoPoliticoService;

    private PartidoPolitico partido1;
    private PartidoPolitico partido2;

    @BeforeEach
    void setUp() {
        partido1 = new PartidoPolitico();
        partido1.setId(1L);
        partido1.setNombre("Partido 1");

        partido2 = new PartidoPolitico();
        partido2.setId(2L);
        partido2.setNombre("Partido 2");
    }

    @Test
    @DisplayName("Test de cargado de datos")
    void testInitSuccess() {

        List<PartidoPolitico> listaPartidos = List.of(partido1, partido2);
        given(partidoPoliticoRepository.saveAll(listaPartidos)).willReturn(listaPartidos);

        List<PartidoPolitico> resultado = partidoPoliticoService.init(listaPartidos);

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(2, resultado.size())
        );

        verify(partidoPoliticoRepository, times(1)).saveAll(listaPartidos);
    }

    @Test
    @DisplayName("Test de busqueda de partidos")
    void testGetPartidosSuccess(){

        List<PartidoPolitico> listaPartidos = List.of(partido1);

        given(partidoPoliticoRepository.findAll()).willReturn(listaPartidos);

        partidoPoliticoRepository.saveAll(listaPartidos);
        List<PartidoPoliticoDTO> resultado = partidoPoliticoService.getPartidos();

        assertAll(
    () -> assertNotNull(resultado),
                () -> assertEquals(1, resultado.size())
        );

        verify(partidoPoliticoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test de busqueda de partido por id")
    void testGetPartidoByIdSuccess()
            throws PartidoNoEncontradoException {

        Long idPartido = 1L;
        List<PartidoPolitico> listaPartidos = List.of(partido1);

        given(partidoPoliticoRepository.findById(idPartido)).willReturn(Optional.ofNullable(partido1));

        PartidoPoliticoDTO resultado = partidoPoliticoService.getPartidoById(idPartido);

    }
}
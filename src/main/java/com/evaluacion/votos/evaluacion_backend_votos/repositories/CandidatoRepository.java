package com.evaluacion.votos.evaluacion_backend_votos.repositories;

import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import com.evaluacion.votos.evaluacion_backend_votos.models.PartidoPolitico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

   public List<Candidato> findByPartidoPolitico(PartidoPolitico partidoPolitico);
}

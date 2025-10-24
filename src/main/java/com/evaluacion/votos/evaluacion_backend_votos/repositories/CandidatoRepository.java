package com.evaluacion.votos.evaluacion_backend_votos.repositories;

import com.evaluacion.votos.evaluacion_backend_votos.models.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatoRepository extends JpaRepository<Candidato, Long> {

}

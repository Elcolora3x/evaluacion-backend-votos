package com.evaluacion.votos.evaluacion_backend_votos.repositories;

import com.evaluacion.votos.evaluacion_backend_votos.models.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

}

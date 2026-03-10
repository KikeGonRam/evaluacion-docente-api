package mx.utvt.evaluaciondocente.repository;

import mx.utvt.evaluaciondocente.entity.Cuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Long> {
}


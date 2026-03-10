package mx.utvt.evaluaciondocente.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "evaluaciones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "docente_id", nullable = false)
    private Docente docente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cuestionario_id", nullable = false)
    private Cuestionario cuestionario;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "evaluacion_respuestas", joinColumns = @JoinColumn(name = "evaluacion_id"))
    @Column(name = "respuesta")
    private List<Integer> respuestas;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentarioGeneral;

    @Column(nullable = false)
    private Double puntajeFinal;

    @Column(nullable = false)
    private LocalDateTime fechaEvaluacion;

    @PrePersist
    public void prePersist() {
        this.fechaEvaluacion = LocalDateTime.now();
        if (respuestas != null && !respuestas.isEmpty()) {
            this.puntajeFinal = respuestas.stream()
                    .mapToInt(Integer::intValue)
                    .average()
                    .orElse(0.0);
        }
    }
}

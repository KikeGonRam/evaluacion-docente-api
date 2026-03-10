package mx.utvt.evaluaciondocente.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "cuestionarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "cuestionario_preguntas", joinColumns = @JoinColumn(name = "cuestionario_id"))
    @Column(name = "pregunta")
    private List<String> preguntas;
}


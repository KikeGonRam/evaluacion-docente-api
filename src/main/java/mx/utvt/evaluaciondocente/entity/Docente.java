package mx.utvt.evaluaciondocente.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "docentes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String materia;

    @Column(nullable = false, unique = true)
    private String email;
}


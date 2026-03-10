# Modelo de Datos - Sistema de Evaluación Docente

## Diagrama de Entidades

```
┌─────────────────────┐
│    ESTUDIANTE       │
├─────────────────────┤
│ id (PK)             │
│ nombre              │
│ matricula (UNIQUE)  │
│ email (UNIQUE)      │
└──────────┬──────────┘
           │
           │ 1
           │
           │ N
┌──────────┴──────────────────────────────────────┐
│                 EVALUACION                      │
├─────────────────────────────────────────────────┤
│ id (PK)                                         │
│ estudiante_id (FK) ──────────────────┐          │
│ docente_id (FK) ─────────┐           │          │
│ cuestionario_id (FK) ──┐ │           │          │
│ respuestas (List<Int>) │ │           │          │
│ comentarioGeneral      │ │           │          │
│ puntajeFinal          │ │           │          │
│ fechaEvaluacion       │ │           │          │
└───────────────────────┼─┼───────────┼──────────┘
                        │ │           │
                      N │ │ N         │ N
                        │ │           │
           ┌────────────┘ │           │
           │              │           │
           │ 1            │ 1         │
┌──────────┴──────────┐   │           │
│   CUESTIONARIO      │   │           │
├─────────────────────┤   │           │
│ id (PK)             │   │           │
│ titulo              │   │           │
│ preguntas (List)    │   │           │
└─────────────────────┘   │           │
                          │           │
                          │ 1         │
                ┌─────────┴───────┐   │
                │    DOCENTE      │   │
                ├─────────────────┤   │
                │ id (PK)         │   │
                │ nombre          │   │
                │ materia         │   │
                │ email (UNIQUE)  │   │
                └─────────────────┘   │
```

## Entidades Detalladas

### 1. ESTUDIANTE
**Tabla:** `estudiantes`

| Campo      | Tipo   | Restricciones           | Descripción                    |
|------------|--------|-------------------------|--------------------------------|
| id         | Long   | PK, AUTO_INCREMENT      | Identificador único            |
| nombre     | String | NOT NULL                | Nombre completo del estudiante |
| matricula  | String | NOT NULL, UNIQUE        | Matrícula institucional        |
| email      | String | NOT NULL, UNIQUE        | Correo electrónico             |

**Relaciones:**
- 1 Estudiante → N Evaluaciones

---

### 2. DOCENTE
**Tabla:** `docentes`

| Campo   | Tipo   | Restricciones           | Descripción                 |
|---------|--------|-------------------------|-----------------------------|
| id      | Long   | PK, AUTO_INCREMENT      | Identificador único         |
| nombre  | String | NOT NULL                | Nombre completo del docente |
| materia | String | NOT NULL                | Materia que imparte         |
| email   | String | NOT NULL, UNIQUE        | Correo electrónico          |

**Relaciones:**
- 1 Docente → N Evaluaciones

---

### 3. CUESTIONARIO
**Tabla:** `cuestionarios`

| Campo     | Tipo         | Restricciones      | Descripción                      |
|-----------|--------------|--------------------|----------------------------------|
| id        | Long         | PK, AUTO_INCREMENT | Identificador único              |
| titulo    | String       | NOT NULL           | Título del cuestionario          |
| preguntas | List<String> | ElementCollection  | Lista de preguntas del formulario|

**Tabla relacionada:** `cuestionario_preguntas`
- cuestionario_id (FK)
- pregunta (String)

**Relaciones:**
- 1 Cuestionario → N Evaluaciones

---

### 4. EVALUACION
**Tabla:** `evaluaciones`

| Campo              | Tipo           | Restricciones      | Descripción                                |
|--------------------|----------------|--------------------|-------------------------------------------|
| id                 | Long           | PK, AUTO_INCREMENT | Identificador único                       |
| estudiante_id      | Long           | FK, NOT NULL       | Referencia a Estudiante                   |
| docente_id         | Long           | FK, NOT NULL       | Referencia a Docente                      |
| cuestionario_id    | Long           | FK, NOT NULL       | Referencia a Cuestionario                 |
| respuestas         | List<Integer>  | ElementCollection  | Calificaciones numéricas por pregunta     |
| comentarioGeneral  | String         | NOT NULL, TEXT     | Comentarios adicionales del estudiante    |
| puntajeFinal       | Double         | NOT NULL           | Promedio calculado de respuestas          |
| fechaEvaluacion    | LocalDateTime  | NOT NULL           | Timestamp de creación (auto-generado)     |

**Tabla relacionada:** `evaluacion_respuestas`
- evaluacion_id (FK)
- respuesta (Integer)

**Relaciones:**
- N Evaluaciones → 1 Estudiante
- N Evaluaciones → 1 Docente
- N Evaluaciones → 1 Cuestionario

**Lógica de negocio (@PrePersist):**
- `fechaEvaluacion` se establece automáticamente con la fecha/hora actual
- `puntajeFinal` se calcula como el promedio de todas las respuestas

---

## Reglas de Integridad

1. **Unicidad:**
   - Email de estudiantes debe ser único
   - Matrícula de estudiantes debe ser única
   - Email de docentes debe ser único

2. **Obligatoriedad:**
   - Todos los campos son NOT NULL excepto donde se indique
   - Una evaluación debe tener estudiante, docente y cuestionario

3. **Cascada:**
   - Las colecciones (preguntas, respuestas) se gestionan con EAGER fetch
   - Las relaciones ManyToOne también usan EAGER fetch para optimizar consultas

4. **Cálculos automáticos:**
   - El puntaje final se calcula automáticamente antes de persistir
   - La fecha de evaluación se asigna automáticamente

---

## Ejemplos de Datos

### Estudiante
```json
{
  "id": 1,
  "nombre": "Luis González Ramírez",
  "matricula": "2024001",
  "email": "luis.gonzalez@utvt.edu.mx"
}
```

### Docente
```json
{
  "id": 1,
  "nombre": "Jorge Almeida Montiel",
  "materia": "Programación Web",
  "email": "jorge.almeida@utvt.edu.mx"
}
```

### Cuestionario
```json
{
  "id": 1,
  "titulo": "Evaluación Docente 2024-A",
  "preguntas": [
    "¿El docente domina la materia?",
    "¿Explica con claridad?",
    "¿Resuelve dudas adecuadamente?",
    "¿Es puntual en sus clases?",
    "¿Evalúa de manera justa?"
  ]
}
```

### Evaluacion
```json
{
  "id": 1,
  "estudiante": {
    "id": 1,
    "nombre": "Luis González Ramírez"
  },
  "docente": {
    "id": 1,
    "nombre": "Jorge Almeida Montiel"
  },
  "cuestionario": {
    "id": 1,
    "titulo": "Evaluación Docente 2024-A"
  },
  "respuestas": [5, 4, 5, 5, 4],
  "comentarioGeneral": "Excelente docente, muy claro en sus explicaciones",
  "puntajeFinal": 4.6,
  "fechaEvaluacion": "2024-03-09T14:30:00"
}
```


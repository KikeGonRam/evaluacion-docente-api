package mx.utvt.evaluaciondocente.mapper;

import mx.utvt.evaluaciondocente.dto.EstudianteDTO;
import mx.utvt.evaluaciondocente.entity.Estudiante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EstudianteMapper {

    EstudianteDTO toDTO(Estudiante estudiante);

    @Mapping(target = "id", ignore = true)
    Estudiante toEntity(EstudianteDTO dto);
}

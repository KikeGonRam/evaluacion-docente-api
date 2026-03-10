package mx.utvt.evaluaciondocente.mapper;

import mx.utvt.evaluaciondocente.dto.CuestionarioDTO;
import mx.utvt.evaluaciondocente.entity.Cuestionario;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CuestionarioMapper {

    CuestionarioDTO toDTO(Cuestionario cuestionario);

    Cuestionario toEntity(CuestionarioDTO dto);
}


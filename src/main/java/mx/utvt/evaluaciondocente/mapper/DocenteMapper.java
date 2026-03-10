package mx.utvt.evaluaciondocente.mapper;

import mx.utvt.evaluaciondocente.dto.DocenteDTO;
import mx.utvt.evaluaciondocente.entity.Docente;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocenteMapper {

    DocenteDTO toDTO(Docente docente);

    Docente toEntity(DocenteDTO dto);
}


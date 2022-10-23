package hu.lacztam.hallgatoi.mapper;

import org.mapstruct.Mapper;

import hu.lacztam.hallgatoi.dto.TeacherDto;
import hu.lacztam.hallgatoi.model.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
	
	TeacherDto teacherToDto(Teacher teacher);
	
}

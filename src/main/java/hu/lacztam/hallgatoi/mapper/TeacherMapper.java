package hu.lacztam.hallgatoi.mapper;

import org.mapstruct.Mapper;

import hu.lacztam.hallgatoi.api.model.TeacherDto;
import hu.lacztam.hallgatoi.model.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {
	
	TeacherDto teacherToDto(Teacher teacher);
	
}

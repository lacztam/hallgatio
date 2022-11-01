package hu.lacztam.hallgatoi.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.lacztam.hallgatoi.api.model.HistoryDataStudentDto;
import hu.lacztam.hallgatoi.api.model.StudentDto;
import hu.lacztam.hallgatoi.model.Student;


@Mapper(componentModel = "spring")
public interface StudentMapper {
	
	StudentDto studentToDto(Student student);
	
	Student dtoToStudent(StudentDto studentDto);
	
	List<StudentDto> studentsToDtos(List<Student> students);

	HistoryDataStudentDto studentHistoryDataToDto(Student data);
	
}
package hu.lacztam.hallgatoi.dto;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CourseDto {

	private int id;
	private String name;
	private List<StudentDto> students;
	private List<TeacherDto> teachers;
	
}

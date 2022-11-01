package hu.lacztam.hallgatoi.mapper;

import java.util.List;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.model.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {
	
//	@Named("summary")
//	@Mapping(ignore = true, target = "teacherDtos")
//	@Mapping(ignore = true, target = "studentDtos")
//	CourseDto courseSummaryToDto(Course course);
//	@IterableMapping(qualifiedByName = "summary")
//	List<CourseDto> courseSummariesToDtos(Iterable<Course> courses);
//	
//	//
//	
//	CourseDto courseToDto(Course course);
//	Course dtoToCourse(CourseDto courseDto);
//	List<CourseDto> courseToDtos(Iterable<Course> courses);
	
	CourseDto courseToDto(Course course);
	
	@Named("summary")
	@Mapping(ignore = true, target = "students")
	@Mapping(ignore = true, target = "teachers")
	CourseDto courseSummaryToDto(Course course);

	Course dtoToCourse(CourseDto courseDto);

	List<CourseDto> coursesToDtos(Iterable<Course> courses);

	@IterableMapping(qualifiedByName = "summary")
	List<CourseDto> courseSummariesToDtos(Iterable<Course> findAll);
	
}

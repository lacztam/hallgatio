package hu.lacztam.hallgatoi.mapper;

import org.mapstruct.Mapper;

import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.api.model.HistoryDataCourseDto;
import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.HistoryData;

@Mapper(componentModel = "spring")
public interface HistoryDataMapper {

	HistoryDataCourseDto courseHistoryDataToDto(HistoryData<Course> hd);

}

package hu.lacztam.hallgatoi.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import hu.lacztam.hallgatoi.api.HistoryControllerApi;
import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.api.model.HistoryDataCourseDto;
import hu.lacztam.hallgatoi.api.model.HistoryDataStudentDto;
import hu.lacztam.hallgatoi.api.model.StudentDto;
import hu.lacztam.hallgatoi.mapper.CourseMapper;
import hu.lacztam.hallgatoi.mapper.HistoryDataMapper;
import hu.lacztam.hallgatoi.mapper.StudentMapper;
import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.HistoryData;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.service.CourseService;
import hu.lacztam.hallgatoi.service.HistoryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HistoryController implements HistoryControllerApi {

	private final CourseMapper courseMapper;
	private final CourseService courseService;
	private final HistoryService historyService;
	private final StudentMapper studentMapper;
	private final HistoryDataMapper historyDataMapper;

	@Override
	public Optional<NativeWebRequest> getRequest() {
		// TODO Auto-generated method stub
		return HistoryControllerApi.super.getRequest();
	}

	@Override
	public ResponseEntity<List<HistoryDataCourseDto>> getCourseHistoryById(Integer id) {
		List<HistoryData<Course>> courses = courseService.getCourseHistory(id);

		List<HistoryDataCourseDto> courseDtosWithHistory = new ArrayList<>();

		courses.forEach(hd -> {
			courseDtosWithHistory.add(historyDataMapper.courseHistoryDataToDto(hd));
		});

		return ResponseEntity.ok(courseDtosWithHistory);
	}

	@Override
	public ResponseEntity<HistoryDataStudentDto> getStudentHistoryById(Integer id, Long timeInMilli) {
		HistoryData<Student> studentHistory = historyService.getStudentHistory(id, timeInMilli);
		
		HistoryDataStudentDto historyDataStudentDto = studentMapper.studentHistoryDataToDto(studentHistory.getData());
		
		return ResponseEntity.ok(historyDataStudentDto);
	}

}

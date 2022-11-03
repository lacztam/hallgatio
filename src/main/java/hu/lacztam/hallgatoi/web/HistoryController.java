package hu.lacztam.hallgatoi.web;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		HistoryData<Student> studentHistory = historyService.getStudentHistoryLT(id, timeInMilli);
		
		HistoryDataStudentDto historyDataStudentDto = studentMapper.studentHistoryDataToDto(studentHistory.getData());
		
		return ResponseEntity.ok(historyDataStudentDto);
	}
	
	@GetMapping("/api/history/{id}/student2/{at}")
	public StudentDto getStudentHistoryById2(@PathVariable int id, @PathVariable LocalDateTime at) {
		Student studentAt = historyService.getStudentHistoryWebuni(id, at);
		System.out.println(studentAt);
		
		StudentDto studentDto = studentMapper.studentToDto(studentAt);
		System.out.println(studentDto);
		
//		HistoryData<StudentDto> studentDtoHistory 
//			= new HistoryData<StudentDto>(
//				studentDto, 
//				studentHistory.getRevisionType(), 
//				studentHistory.getRevision(), 
//				studentHistory.getDate());
		
		return studentDto;
	}

}

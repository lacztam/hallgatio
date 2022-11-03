package hu.lacztam.hallgatoi.web;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.api.model.StudentDto;
import hu.lacztam.hallgatoi.mapper.CourseMapper;
import hu.lacztam.hallgatoi.mapper.StudentMapper;
import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.HistoryData;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.service.CourseService;
import hu.lacztam.hallgatoi.service.HistoryService;
import lombok.RequiredArgsConstructor;


//@RestController
@RequestMapping("/api/history")
@RequiredArgsConstructor
public class HistoryControllerOld {
	
	private final CourseMapper courseMapper;
	private final CourseService courseService;
	private final HistoryService historyService;
	private final StudentMapper studentMapper;
	
	@GetMapping("/{id}/course")
	public List<HistoryData<CourseDto>> getCourseHistoryById(@PathVariable int id) {
		List<HistoryData<Course>> courses = courseService.getCourseHistory(id); 
	
		List<HistoryData<CourseDto>> courseDtosWithHistory = new ArrayList<>();
		
		courses.forEach(hd -> {
			courseDtosWithHistory.add(new HistoryData<>(
				courseMapper.courseToDto(hd.getData()),
				hd.getRevisionType(),
				hd.getRevision(),
				hd.getDate()
				));
		});
		
		return courseDtosWithHistory;
	}
	
	@GetMapping("/{id}/student/{timeInMilli}")
	public HistoryData<StudentDto> getStudentHistoryById(@PathVariable int id, @PathVariable long timeInMilli) {
		HistoryData<Student> studentHistory = historyService.getStudentHistoryLT(id, timeInMilli);
		
		StudentDto studentDto = studentMapper.studentToDto(studentHistory.getData());
		
		HistoryData<StudentDto> studentDtoHistory 
			= new HistoryData<StudentDto>(
				studentDto, 
				studentHistory.getRevisionType(), 
				studentHistory.getRevision(), 
				studentHistory.getDate());
		
		return studentDtoHistory;
	}
	
	@GetMapping("/{id}/student2/{timeInMilli}")
	public StudentDto getStudentHistoryById2(@PathVariable int id, @NotNull @Valid OffsetDateTime at) {
//		Student studentAt = historyService.getStudentHistoryWebuni(id, at);
//		
//		StudentDto studentDto = studentMapper.studentToDto(studentAt);
		
//		HistoryData<StudentDto> studentDtoHistory 
//			= new HistoryData<StudentDto>(
//				studentDto, 
//				studentHistory.getRevisionType(), 
//				studentHistory.getRevision(), 
//				studentHistory.getDate());
		
//		return studentDto;
		return null;
	}
	
	
}

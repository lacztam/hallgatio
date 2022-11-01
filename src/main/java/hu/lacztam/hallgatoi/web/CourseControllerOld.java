package hu.lacztam.hallgatoi.web;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.querydsl.core.types.Predicate;

import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.mapper.CourseMapper;
import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.repository.CourseRepository;
import hu.lacztam.hallgatoi.service.CourseService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
//@RestController
@RequestMapping("/api/course")
public class CourseControllerOld {
	
	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	private final CourseService courseService;
	
	@GetMapping("/search")    
	public List<CourseDto> searchCourse(
			@QuerydslPredicate(root = Course.class) Predicate predicate, 
			@RequestParam Optional<Boolean> full,
			@SortDefault("id") Pageable pageable) {
		boolean isSummaryNeeded = full.isEmpty() || !full.get();
		
		//Iterable<Course> result = courseRepository.findAll(predicate);
		Iterable<Course> result = isSummaryNeeded ? courseRepository.findAll(predicate, pageable) : courseService.searchCourses(predicate, pageable);
	
		if(isSummaryNeeded) {
			return  courseMapper.courseSummariesToDtos(result);
		}else {
			return courseMapper.coursesToDtos(result);
		}
	}
	
	@PutMapping("/{courseId}")
	public ResponseEntity<CourseDto> modifyCourse(
			@PathVariable int courseId, 
			@RequestBody CourseDto courseDto) {
			
			Course course = courseMapper.dtoToCourse(courseDto);
			course.setId(courseId);
			
			try {
				CourseDto savedCourseDto = courseMapper.courseSummaryToDto(courseService.update(course));

				return ResponseEntity.ok(savedCourseDto);
			} catch (NoSuchElementException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			}
	}
	
}

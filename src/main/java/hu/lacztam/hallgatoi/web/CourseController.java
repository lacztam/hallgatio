package hu.lacztam.hallgatoi.web;

import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

import com.querydsl.core.types.Predicate;

import hu.lacztam.hallgatoi.api.CourseControllerApi;
import hu.lacztam.hallgatoi.api.model.CourseDto;
import hu.lacztam.hallgatoi.mapper.CourseMapper;
import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.repository.CourseRepository;
import hu.lacztam.hallgatoi.service.CourseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CourseController implements CourseControllerApi {
	
	private final NativeWebRequest nativeWebRequest;
	private final CourseRepository courseRepository;
	private final CourseMapper courseMapper;
	private final CourseService courseService;
	private final PageableHandlerMethodArgumentResolver pageableResolver;
	private final QuerydslPredicateArgumentResolver predicateResolver;
	
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.of(nativeWebRequest);
	}

	@Override
	public ResponseEntity<CourseDto> modifyCourse(Integer courseId, @Valid CourseDto courseDto) {
		Course course = courseMapper.dtoToCourse(courseDto);
		course.setId(courseId);
		
		try {
			CourseDto savedCourseDto = courseMapper.courseSummaryToDto(courseService.update(course));

			return ResponseEntity.ok(savedCourseDto);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	public void configPageable(@SortDefault("id") Pageable pageable) {	}

	
	public void configurePredicate(@QuerydslPredicate(root = Course.class) Predicate predicate) { }
	
	@Override
	public ResponseEntity<List<CourseDto>> searchCourse(
			@Valid Boolean full, 
			@Valid Integer page, 
			@Valid Integer size,
			@Valid String sort) {
		
		Method method = null;
		try {
			method = this.getClass().getMethod("configPageable", Pageable.class);
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		MethodParameter methodParameter = new MethodParameter(method, 0);
		ModelAndViewContainer mavContainer = null;
		WebDataBinderFactory binderFactory = null;
		Pageable pageable = pageableResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
		
		boolean isSummaryNeeded = full == null ? false : full;
		
		Predicate predicate = createPredicate("configurePredicate");
		
		//Iterable<Course> result = courseRepository.findAll(predicate);
		Iterable<Course> result 
			= isSummaryNeeded ? courseRepository.findAll(predicate, pageable) : courseService.searchCourses(predicate, pageable);
	
		if(isSummaryNeeded) {
			return  ResponseEntity.ok(courseMapper.courseSummariesToDtos(result));
		}else {
			return ResponseEntity.ok(courseMapper.coursesToDtos(result));
		}
	}

	private Predicate createPredicate(String configMethodName) {
		Method method;
		try {
			method = this.getClass().getMethod(configMethodName, Predicate.class);
			MethodParameter methodParameter = new MethodParameter(method, 0);
			ModelAndViewContainer mavContainer = null;
			WebDataBinderFactory binderFactory = null;
			
			return (Predicate) predicateResolver.resolveArgument(methodParameter, mavContainer, nativeWebRequest, binderFactory);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}

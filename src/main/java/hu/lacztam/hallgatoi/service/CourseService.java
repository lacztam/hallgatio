package hu.lacztam.hallgatoi.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.HistoryData;
import hu.lacztam.hallgatoi.model.QCourse;
import hu.lacztam.hallgatoi.repository.CourseRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CourseService {

	private final CourseRepository courseRepository;
	@PersistenceContext
	private EntityManager em;
	
	public Course saveCourse(Course course) {
		if(course == null)
			throw new NullPointerException();
		
		return courseRepository.save(course);
	}
	
	@Transactional
	@Cacheable("pagedCoursessWithRelationships")
	public List<Course> searchCourses(Predicate predicate, Pageable pageable){
		
		/*
		List<Course> courses = courseRepository.findAll(predicate, "Course.students", EntityGraphType.LOAD);
		courses = courseRepository.findAll(QCourse.course.in(courses), "Course.teachers", EntityGraphType.LOAD);
		*/
		
		Page<Course> coursePage = courseRepository.findAll(predicate, pageable);

		BooleanExpression inPredicate = QCourse.course.in(coursePage.getContent());

		List<Course> courses = courseRepository.findAll(inPredicate, "Course.students", EntityGraphType.LOAD, Sort.unsorted());
		
		courses = courseRepository.findAll(inPredicate, "Course.teachers", EntityGraphType.LOAD, pageable.getSort());
		
		return courses;
	}
	
	@Transactional
	@SuppressWarnings({"rawtypes", "unchecked"})
	public List<HistoryData<Course>> getCourseHistory(int id){
		
		List resultList = AuditReaderFactory.get(em)
		.createQuery()
		.forRevisionsOfEntity(Course.class, false, true)
		.add(AuditEntity.property("id").eq(id))
		.getResultList()
		.stream()
		.map(o -> {
			Object[] objArray = (Object[])o;
			DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
			Course course = (Course)objArray[0];
			
			course.getName();
			course.getStudents().size();
			course.getTeachers().size();
			
			return new HistoryData<Course>(
					course,
					(RevisionType)objArray[2],
					revisionEntity.getId(),
					revisionEntity.getRevisionDate()
					);
		}).toList();
		
		return resultList; 
	}

	public Course update(Course course) {
		courseExistsById(course.getId());
		return courseRepository.save(course);
	}
	
	public Course courseExistsById(int id) {
		Optional<Course> courseOptional = courseRepository.findById(id);
		if(courseOptional.isPresent())
			return courseOptional.get();
		 else 
			throw new NoSuchElementException();
	}

}

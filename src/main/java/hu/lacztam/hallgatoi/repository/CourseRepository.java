package hu.lacztam.hallgatoi.repository;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;


import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.QCourse;

public interface CourseRepository extends
	JpaRepository<Course, Integer>, 
	JpaSpecificationExecutor<Course>, 
	QuerydslPredicateExecutor<Course>,
	QuerydslBinderCustomizer<QCourse>,
	QueryDslWithEntityGraphRepository<Course, Integer>{
	
	
	//1. Descartes szorzat miatt, túl sok eredmény sor a DB-ből
	//@EntityGraph(attributePaths = {"teachers, students"})
	//List<Course> findAllWithTeachersAndStudents(Predicate predicate);
	
	//2. Itt már nincs Descartes szorzat, de nem tudja a Spring Data Querydsl támogatni
	//a predicate-et elfogadó metódusból egy van (a findAll) a custom metódus nem működik együtt a predicate-el + entitygraph-al
	// --> fel kell okosítani a repositor-y olyan módon, hogy elfogadjon predicate-et és egy entitygraph-nak a nevét
	//@EntityGraph(attributePaths = {"teachers"})
	//List<Course> findAllWithTeachersAndStudents(Predicate predicate);
	
	//@EntityGraph(attributePaths = {"students"})
	//List<Course> findAllWithTeachersAndStudents(Predicate predicate);
		

	@Override
	default void customize(QuerydslBindings bindings, QCourse course) {
		
		bindings.bind(course.name).first((path, value) -> path.startsWithIgnoreCase(value));
		
		bindings.bind(course.teachers.any().name).first((path, value) -> path.startsWithIgnoreCase(value));
		
		bindings.bind(course.students.any().semester).all((path, values) -> {
			if(values.size() != 2)
				return Optional.empty();
			
			Iterator<? extends Integer> iterator = values.iterator();
			Integer from = iterator.next();
			Integer to = iterator.next();
			
			return Optional.of(path.between(from, to));
			
		});
		
	}

}

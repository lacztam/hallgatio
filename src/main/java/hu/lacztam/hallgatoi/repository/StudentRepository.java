package hu.lacztam.hallgatoi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import hu.lacztam.hallgatoi.model.Student;


public interface StudentRepository extends JpaRepository<Student, Integer>{

	@Query("SELECT s from Student s "
			+ "WHERE s.centralIdentifier = :centralIdentifier")
	Optional<Student> findbyCentralIdentifier(int centralIdentifier);

}

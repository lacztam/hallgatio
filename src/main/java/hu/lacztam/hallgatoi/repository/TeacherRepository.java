package hu.lacztam.hallgatoi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;

import hu.lacztam.hallgatoi.model.QTeacher;
import hu.lacztam.hallgatoi.model.Teacher;

public interface TeacherRepository extends 
	JpaRepository<Teacher, Integer>{
	
}

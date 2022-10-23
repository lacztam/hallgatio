package hu.lacztam.hallgatoi.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@NamedEntityGraph(
		name = "Course.students",
		attributeNodes = @NamedAttributeNode("students"))
@NamedEntityGraph(
		name = "Course.teachers",
		attributeNodes = @NamedAttributeNode("teachers"))
@Entity
@Cacheable
@Audited
public class Course {

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Include
	@ToString.Include
	private int id;
	
	@ToString.Include
	private String name;
	
	@ManyToMany
	private Set<Student> students;
	
	@ManyToMany
	private Set<Teacher> teachers;
	
	public void addStudent(Student student) {
		if(this.students == null) 
			this.students = new HashSet<>();
		
		this.students.add(student);
	}
	
	public void addTeacher(Teacher teacher) {
		if(this.teachers == null) 
			this.teachers = new HashSet<>();	
		
		this.teachers.add(teacher);
	}
	
}

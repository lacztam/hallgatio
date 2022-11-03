package hu.lacztam.hallgatoi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.model.Teacher;
import hu.lacztam.hallgatoi.repository.CourseRepository;
import hu.lacztam.hallgatoi.repository.StudentRepository;
import hu.lacztam.hallgatoi.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class InitDbService {

	private final StudentRepository studentRepository;
	private final TeacherRepository teacherRepository;
	private final CourseRepository courseRepository;
	private final StudentService studentService;
	
	@Transactional
	public void deleteDb() {
		courseRepository.deleteAll();
		studentRepository.deleteAll();
		teacherRepository.deleteAll();
	}
	
	//duplikaltan hozza letre az mind a harom entitast (tanlulok, tanarok, kurzusok)
	@Transactional
	public void createStudents(){
		Student student1 = new Student(0, "Klári", LocalDate.now().minusYears(20), 2, ThreadLocalRandom.current().nextInt(100, 999 + 1), 0);
		Student student2 = new Student(0, "Panka", LocalDate.now().minusYears(21), 3, ThreadLocalRandom.current().nextInt(100, 999 + 1), 1);
		Student student3 = new Student(0, "Ilona", LocalDate.now().minusYears(18), 4, ThreadLocalRandom.current().nextInt(100, 999 + 1), 2);
		Student student4 = new Student(0, "Anna", LocalDate.now().minusYears(19), 1, ThreadLocalRandom.current().nextInt(100, 999 + 1), 3);
		
		studentService.save(student1);
		studentService.save(student2);
		studentService.save(student3);
		studentRepository.save(student4);
	}
	
	@Transactional
	public void createTeachers() {
		Teacher teacher1 = new Teacher(0, "Péter", LocalDate.now().minusYears(32));
		Teacher teacher2 = new Teacher(0, "Mária", LocalDate.now().minusYears(41));
		Teacher teacher3 = new Teacher(0, "Géza", LocalDate.now().minusYears(56));
		
		teacherRepository.save(teacher1);
		teacherRepository.save(teacher2);
		teacherRepository.save(teacher3);
		
	}
	
	@Transactional
	public void createCourses() {
		List<Student> students = studentRepository.findAll();
		List<Teacher> teachers = teacherRepository.findAll();
		
		Course course1 = new Course();
		course1.setName("Matematika");
		course1.addStudent(students.get(0));
		course1.addStudent(students.get(1));
		course1.addTeacher(teachers.get(0));
		
		Course course2 = new Course();
		course2.setName("Fizika");
		course2.addStudent(students.get(1));
		course2.addTeacher(teachers.get(1));
		
		Course course3 = new Course();
		course3.setName("Filozófia");
		course3.addStudent(students.get(2));
		course3.addStudent(students.get(3));
		course3.addTeacher(teachers.get(2));
		
		courseRepository.save(course1);
		courseRepository.save(course2);
		courseRepository.save(course3);
		
	}
	
	@Transactional
	public void modifyStudent() {
		List<Student> students = studentRepository.findAll();
		Student student = students.get(0);
		student.setName("AbrakManó");
		studentRepository.save(student);
	}
	
}

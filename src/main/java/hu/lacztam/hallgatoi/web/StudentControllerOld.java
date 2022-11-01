package hu.lacztam.hallgatoi.web;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.lacztam.hallgatoi.api.model.StudentDto;
import hu.lacztam.hallgatoi.mapper.StudentMapper;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.repository.StudentRepository;
import hu.lacztam.hallgatoi.service.InitDbService;
import hu.lacztam.hallgatoi.service.StudentService;
import lombok.AllArgsConstructor;

//@RestController
@RequestMapping("/api/student")
@AllArgsConstructor
public class StudentControllerOld {
	
	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;
	private final StudentService studentService;
	private final InitDbService initDbService;

	@GetMapping
	public List<StudentDto> getEveryStudent(){
		return studentMapper.studentsToDtos(studentRepository.findAll());
	}
	
	@PutMapping("/{id}")
	public StudentDto modifyStudent(@PathVariable int id, @RequestBody StudentDto studentDto) {

		Student student = studentMapper.dtoToStudent(studentDto);
		
		student.setId(id);
		try {
			StudentDto savedStudentDto = studentMapper.studentToDto(studentService.update(student));
			
			return savedStudentDto;
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/delete_all")
	public void deleteAllStudent() {
		initDbService.deleteDb();
	}
	
	@PostMapping("/insert_students")
	public void insertStudents() {
		initDbService.createStudents();
//		initDbService.createTeachers();
//		initDbService.createCourses();
	}
	
}

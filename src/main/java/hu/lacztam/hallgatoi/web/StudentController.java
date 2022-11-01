package hu.lacztam.hallgatoi.web;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import hu.lacztam.hallgatoi.api.StudentControllerApi;
import hu.lacztam.hallgatoi.api.model.StudentDto;
import hu.lacztam.hallgatoi.mapper.StudentMapper;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.repository.StudentRepository;
import hu.lacztam.hallgatoi.service.InitDbService;
import hu.lacztam.hallgatoi.service.StudentService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StudentController implements StudentControllerApi {
	
	private final StudentRepository studentRepository;
	private final StudentMapper studentMapper;
	private final StudentService studentService;
	private final InitDbService initDbService;
	private final NativeWebRequest nativeWebRequest;
	
	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.of(nativeWebRequest);
	}
	
	@Override
	public ResponseEntity<StudentDto> modifyStudent(Integer id, @Valid StudentDto studentDto) {
		Student student = studentMapper.dtoToStudent(studentDto);
		
		student.setId(id);
		try {
			StudentDto savedStudentDto = studentMapper.studentToDto(studentService.update(student));
			
			return ResponseEntity.ok(savedStudentDto);
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public ResponseEntity<Void> deleteAllStudent() {
		initDbService.deleteDb();
		
		return ResponseEntity.ok().build();
	}

	@Override
	public ResponseEntity<List<StudentDto>> getEveryStudent() {
		return ResponseEntity.ok(studentMapper.studentsToDtos(studentRepository.findAll()));
	}

	@Override
	public ResponseEntity<Void> insertStudents() {
		initDbService.createStudents();
		initDbService.createTeachers();
		initDbService.createCourses();
		
		return ResponseEntity.ok().build();
	}

}

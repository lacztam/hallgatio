package hu.lacztam.hallgatoi.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import hu.lacztam.hallgatoi.model.Course;
import hu.lacztam.hallgatoi.model.HistoryData;
import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StudentService {

	private final StudentRepository studentRepository;
	@PersistenceContext
	private final EntityManager em;

	@Value("${hallgatoi.profilePics}")
	private String profilePicsFolder;

	@PostConstruct
	public void init() {
		try {
			System.out.println("profilePicsFolder=" + profilePicsFolder);
			Files.createDirectories(Path.of(profilePicsFolder));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Transactional
	public Student findByCentralIdentifier(int centralIdentifier) {
		Optional<Student> optionalStudent = studentRepository.findbyCentralIdentifier(centralIdentifier);
		if (optionalStudent.isPresent()) {
			return optionalStudent.get();
		} else {
			throw new NoSuchElementException();
		}
	}

	@Transactional
	public List<Student> findAll() {
		return studentRepository.findAll();
	}

	@Transactional
	public Student save(Student student) {
		if (student == null)
			throw new NullPointerException();

		return studentRepository.save(student);
	}

	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<HistoryData<Course>> getCourseHistory(int id) {

		List resultList = AuditReaderFactory.get(em).createQuery().forRevisionsOfEntity(Student.class, false, true)
				.add(AuditEntity.property("id").eq(id)).getResultList().stream().map(o -> {
					Object[] objArray = (Object[]) o;
					DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
					Course course = (Course) objArray[0];

					course.getName();
					course.getStudents().size();
					course.getTeachers().size();

					return new HistoryData<Course>(course, (RevisionType) objArray[2], revisionEntity.getId(),
							revisionEntity.getRevisionDate());
				}).toList();

		return resultList;
	}

	public Student update(Student student) {
		Optional<Student> optionalStudent = studentRepository.findById(student.getId());
		if (optionalStudent.isPresent()) {
			return studentRepository.save(student);
		} else {
			throw new NoSuchElementException();
		}
	}

	public void saveProfilePicture(Integer id, InputStream is) {
		if (!studentRepository.existsById(id))
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		try {
			Files.copy(is, getProfilePicPathForStudent(id), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private Path getProfilePicPathForStudent(Integer id) {
		return Paths.get(profilePicsFolder, id.toString() + ".jpg");
	}

	public Resource getProfilePicture(Integer id) {
		FileSystemResource fileSystemResource = new FileSystemResource(getProfilePicPathForStudent(id));
		if (!fileSystemResource.exists()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return fileSystemResource;
	}

	public void deleteProfilePicture(Integer id) {
		FileSystemResource fileSystemResource = new FileSystemResource(getProfilePicPathForStudent(id));
		if (!fileSystemResource.exists())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		try {
			Files.delete(getProfilePicPathForStudent(id));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}

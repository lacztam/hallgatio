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
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}

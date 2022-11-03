package hu.lacztam.hallgatoi.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.lacztam.hallgatoi.model.HistoryData;
import hu.lacztam.hallgatoi.model.Student;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryService {

	@PersistenceContext
	private final EntityManager em;

	@Transactional
	@SuppressWarnings("rawtypes")
	public Student getStudentHistoryWebuni(int id, LocalDateTime when) {
		long epochMillis = when.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

//        long epochMillis = when.toInstant().toEpochMilli();

		List resultList = AuditReaderFactory.get(em).createQuery().forRevisionsOfEntity(Student.class, true, false)
				.add(AuditEntity.property("id").eq(id)).add(AuditEntity.revisionProperty("timestamp").le(epochMillis))
				.addOrder(AuditEntity.revisionProperty("timestamp").desc()).setMaxResults(1).getResultList();

		if (!resultList.isEmpty()) {
			Student student = (Student) resultList.get(0);
			return student;
		}

		return null;
	}

	@Transactional
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HistoryData<Student> getStudentHistoryLT(int id, long timeInMilli) {
		LocalDateTime studentRevisionDate = Instant.ofEpochMilli(timeInMilli).atZone(ZoneId.systemDefault())
				.toLocalDateTime();

		Optional<HistoryData<Student>> optionalHistoryDateofStudent = AuditReaderFactory.get(em).createQuery()
				.forRevisionsOfEntity(Student.class, false, true).add(AuditEntity.property("id").eq(id)).getResultList()
				.stream().map(o -> {
					Object[] objArray = (Object[]) o;
					DefaultRevisionEntity revisionEntity = (DefaultRevisionEntity) objArray[1];
					Student student = (Student) objArray[0];

					if (revisionEntity.getRevisionDate().equals(studentRevisionDate)) {
						return new HistoryData<Student>(student, (RevisionType) objArray[2], revisionEntity.getId(),
								revisionEntity.getRevisionDate());
					}

					return null;
//			return new HistoryData<Student>(
//					student,
//					(RevisionType)objArray[2],
//					revisionEntity.getId(),
//					revisionEntity.getRevisionDate()
//					);
				}).findFirst();
		return optionalHistoryDateofStudent.get();
	}

}

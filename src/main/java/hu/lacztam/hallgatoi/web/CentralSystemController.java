package hu.lacztam.hallgatoi.web;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import hu.lacztam.hallgatoi.model.Student;
import hu.lacztam.hallgatoi.repository.StudentRepository;
import hu.lacztam.hallgatoi.service.CentralSystemService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class CentralSystemController {

	private final StudentRepository studentRepository;
	private final CentralSystemService centralSystemService;
	
	@Scheduled(cron = "${students.cron}")
	public void selectAllStudents() {
		System.out.println("\nstart");
		List<Student> allStudents = studentRepository.findAll();
		for(int i = 0; i < allStudents.size(); i++) {
			System.out.println("\t" + i + ". student is in progress");
			Student student = allStudents.get(i);
			try {
				centralSystemService.mock_usedFreeSemesterByStudent(student.getCentralIdentifier());
				studentRepository.save(student);
				System.out.println("\t" + i + " student saved.");
			} catch (Exception e) {
				System.out.println("\t" + i + "student is not saved.");
			}
		}
		
//		allStudents.forEach(s -> {
//			try {
//				centralSystemService.mock_usedFreeSemesterByStudent(s.getCentralIdentifier());
//				studentRepository.save(s);
//				System.out.println("-Student saved.");
//			} catch (Exception e) {
//				System.out.println("-Student is not saved.");
//			}
//		});
		System.out.println("end");
	}
	
}

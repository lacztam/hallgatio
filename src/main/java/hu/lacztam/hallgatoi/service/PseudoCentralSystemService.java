package hu.lacztam.hallgatoi.service;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hu.lacztam.hallgatoi.aspect.SecureMethodCall;
import lombok.AllArgsConstructor;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class PseudoCentralSystemService {

	private final StudentService studentService;
	
	@SecureMethodCall
	public int mock_usedFreeSemesterByStudent(int centralIdentifier) throws Exception {
		studentService.findByCentralIdentifier(centralIdentifier);
		Random random = new Random();
		
		// minden 2. kérésre hibát dobjon
		int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
		if (randomNum < 50) {
			System.out.println("mock method -> Throwing exception..");
			throw new Exception();
		}
		return random.nextInt(0, 12);
	}

	@Scheduled(cron = "*/10 * * * * *")
	public void selectAllStudents() {
		System.out.println("\nstart");
		studentService.findAll().forEach(s -> {
			try {
				mock_usedFreeSemesterByStudent(s.getCentralIdentifier());
				studentService.save(s);
				System.out.println("Student saved.");
			} catch (Exception e) {
				System.out.println("Student is not saved.");
			}
		});
		System.out.println("end");
	}

}

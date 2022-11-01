package hu.lacztam.hallgatoi.service;

import java.util.Random;

import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import hu.lacztam.hallgatoi.aspect.Retry;
import lombok.AllArgsConstructor;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class CentralSystemService {

	private final StudentService studentService;
	
	@Retry(times = 5, waitTime = 500)
	public int mock_usedFreeSemesterByStudent(int centralIdentifier) throws Exception {
		Random random = new Random();
		int rnd = random.nextInt(0,2);
		if(rnd == 0) {
			throw new RuntimeException("Central system service exception");
		}else {
			return random.nextInt(0,10);
		}
		
//		int randomNum = ThreadLocalRandom.current().nextInt(0, 100 + 1);
//		if (randomNum < 50) {
//			System.out.println("\t\t\tmock method -> Throwing exception..");
//			throw new Exception();
//		}
//		return random.nextInt(0, 12);
	}


}

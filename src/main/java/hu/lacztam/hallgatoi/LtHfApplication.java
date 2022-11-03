package hu.lacztam.hallgatoi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import hu.lacztam.hallgatoi.service.InitDbService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
@EnableCaching
public class LtHfApplication implements CommandLineRunner{

	private final InitDbService initDbService;
	
	public static void main(String[] args){
		SpringApplication.run(LtHfApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initDbService.deleteDb();
		initDbService.createStudents();
		initDbService.createTeachers();
		initDbService.createCourses();
		initDbService.modifyStudent();
	}

}

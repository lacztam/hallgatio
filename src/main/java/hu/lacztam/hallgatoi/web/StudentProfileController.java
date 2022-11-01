package hu.lacztam.hallgatoi.web;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class StudentProfileController {

	public void uploadProfileImage(long id, String filename, @Valid MultipartFile multipartFile) {
		
	}
	
}

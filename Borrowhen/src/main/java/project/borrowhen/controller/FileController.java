package project.borrowhen.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {
	
	@Autowired
	private Environment env;

	@GetMapping(value = "/view/image/{imageName}")
	public @ResponseBody ResponseEntity<byte[]> responseImage(@PathVariable String imageName) {
	    String fileDirectory = env.getProperty("inventory.images.path");
	    Path filePath = Paths.get(fileDirectory + imageName);

	    try {
	        if (!Files.exists(filePath)) {
	            filePath = Paths.get(fileDirectory + "no_image.jpg");
	        }

	        String contentType = Files.probeContentType(filePath);
	        byte[] bytes = Files.readAllBytes(filePath);

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .body(bytes);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new byte[0]);
	    }
	}


}

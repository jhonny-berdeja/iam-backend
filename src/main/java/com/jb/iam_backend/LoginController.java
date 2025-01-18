package com.jb.iam_backend;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class LoginController {

	@PostMapping("/login")
	public ResponseEntity<String> login() {
        log.info("login ok");
        return ResponseEntity.status(HttpStatus.OK).body("logged in to IAM system ok");
	}

}
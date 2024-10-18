package com.dev;

import com.dev.auth.Role;
import com.dev.auth.User;
import com.dev.auth.UserRepository;
import com.dev.core.application.Project;
import com.dev.core.application.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@SpringBootApplication
@RequiredArgsConstructor
@EnableTransactionManagement
@EnableScheduling
public class RedLog {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ProjectRepository projectRepository;

	public static void main(String[] args) {
		SpringApplication.run(RedLog.class, args);
	}
	@PostConstruct
	@Transactional
	public void init(){
		User user=new User();
		user.setUsername("rupesh156@gmail.com");
		user.setPassword(passwordEncoder.encode("rupesh"));
		user.setRole(Role.ADMIN);
		userRepository.save(user);
		if(projectRepository.count()==0){
			Project project= new Project();
			project.setName("spring.boot");
			project.setLogFileExtension(".log");
			project.setLogImportFolder("/Users/rupeshdangi/red-test");
			projectRepository.save(project);
		}
	}

}

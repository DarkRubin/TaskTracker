package org.roadmap.tasktrackerscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class TaskTrackerSchedulerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerSchedulerApplication.class, args);
	}

}

package org.roadmap.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.roadmap.tasktrackerbackend.security.CurrentUserAuthorizationDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final CurrentUserAuthorizationDetails details;

    public void addStartTasks() {
        Task add = new Task("🎯Create your own tasks",
                "For create new task input title above and click 'Add'", details.getCurrentUser());
        Task star = new Task("⭐Set star on Github",
                "Add star to this repository on Github", details.getCurrentUser());
        repository.save(add);
        repository.save(star);
    }

}

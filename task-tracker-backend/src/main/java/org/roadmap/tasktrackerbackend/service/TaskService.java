package org.roadmap.tasktrackerbackend.service;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.mapper.TaskMapper;
import org.roadmap.tasktrackerbackend.mapper.TaskMapperImpl;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.model.User;
import org.roadmap.tasktrackerbackend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper mapper = new TaskMapperImpl();

    private final TaskRepository repository;

    public List<TaskDTO> getTasks(User user) {
        List<TaskDTO> result = new ArrayList<>();
        repository.getAllByOwner(user).forEach(task -> result.add(mapper.toDto(task)));
        return result;
    }

    public List<TaskDTO> getFinished(User owner) {
        List<TaskDTO> result = new ArrayList<>();
        repository.getAllFinishedByOwner(owner).forEach(task -> result.add(mapper.toDto(task)));
        return result;
    }

    public TaskDTO save(String title, String description, User user) {
        Task task = new Task(title, description, user);
        return mapper.toDto(repository.save(task));
    }

    public void finishTask(UUID uuid) {
        repository.updateFinishedTimeByUuid(uuid, Instant.now());
    }

    public void updateText(UUID uuid, String text) {
        repository.updateTextByUuid(uuid, text);
    }

    public void updateTitle(UUID uuid, String title) {
        repository.updateTitleByUuid(uuid, title);
    }

}

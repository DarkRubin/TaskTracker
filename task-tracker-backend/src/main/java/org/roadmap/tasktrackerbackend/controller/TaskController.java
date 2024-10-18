package org.roadmap.tasktrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.exception.InvalidDateParameterException;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.service.TaskService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/tasks")
    public List<Task> getAll() {
        return service.getAll();
    }

    @GetMapping("/tasks/finished")
    public List<Task> getFinished() {
        return service.getAllFinished();
    }

    @GetMapping("/tasks/unfinished")
    public List<Task> getUnfinished() {
        return service.getAllUnfinished();
    }

    @GetMapping("/tasks/unfinished/{date}")
    public List<Task> getUnfinishedByDate(@PathVariable String date) {
        if (date == null || date.isEmpty()) {
            return getUnfinished();
        }
        try {
            Instant from = Instant.parse(date);
            Instant to = from.plus(1, DAYS);
            return service.getAllBetween(from, to);
        } catch (DateTimeParseException e) {
            throw new InvalidDateParameterException();
        }
    }

    @PostMapping("/task")
    public Task add(@RequestBody TaskDTO dto) {
        return service.save(dto);
    }

    @DeleteMapping("/task")
    public void delete(@RequestBody TaskDTO dto) {
        service.delete(dto);
    }

    @PutMapping("/task")
    public Task update(@RequestBody TaskDTO dto) {
        return service.update(service.map(dto));
    }

    @PatchMapping("/task/title")
    public Task updateTitle(@RequestBody TaskDTO dto) {
        Task task = service.getTaskByUuid(dto.uuid());
        task.setTitle(dto.title());
        return service.update(task);
    }

    @PatchMapping("/task/text")
    public Task updateText(@RequestBody TaskDTO dto) {
        Task task = service.getTaskByUuid(dto.uuid());
        task.setText(dto.text());
        return service.update(task);
    }

    @PatchMapping("/task/finish")
    public Task finish(@RequestBody TaskDTO dto) {
        Task task = service.getTaskByUuid(dto.uuid());
        task.setFinishedTime(Instant.now());
        return service.update(task);
    }

    @PatchMapping("/task/continue")
    public Task continueFinished(@RequestBody TaskDTO dto) {
        Task task = service.getTaskByUuid(dto.uuid());
        task.setFinishedTime(null);
        return service.update(task);
    }

}

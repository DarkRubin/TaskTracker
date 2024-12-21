package org.roadmap.tasktrackerbackend.controller;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerbackend.dto.TaskDTO;
import org.roadmap.tasktrackerbackend.model.Task;
import org.roadmap.tasktrackerbackend.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME;

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
    public List<Task> getUnfinishedByDate(@PathVariable @DateTimeFormat(iso = DATE_TIME) Instant date) {
        return service.getAllBetween(date, date.plus(1, DAYS));
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
        return service.save(dto);
    }

    @PatchMapping("/task/title")
    public void updateTitle(@RequestBody TaskDTO dto) {
        service.updateTitle(dto);
    }

    @PatchMapping("/task/text")
    public void updateText(@RequestBody TaskDTO dto) {
        service.updateText(dto);
    }

    @PatchMapping("/task/finish")
    public void finish(@RequestBody TaskDTO dto) {
        service.updateFinishedTime(dto.uuid(), dto.finishedTime());
    }

    @PatchMapping("/task/continue")
    public void continueFinished(@RequestBody TaskDTO dto) {
        service.updateFinishedTime(dto.uuid(), null);
    }

}

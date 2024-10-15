package org.roadmap.tasktrackerscheduler.scheduler;

import lombok.RequiredArgsConstructor;
import org.roadmap.tasktrackerscheduler.dto.DaySummaryDto;
import org.roadmap.tasktrackerscheduler.entity.Task;
import org.roadmap.tasktrackerscheduler.entity.User;
import org.roadmap.tasktrackerscheduler.producer.SummaryProducer;
import org.roadmap.tasktrackerscheduler.repository.TaskRepository;
import org.roadmap.tasktrackerscheduler.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class DailySummaryScheduler {

    private final SummaryProducer summaryProducer;

    private final TaskRepository taskRepository;

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void sendDayReport() {
        for (User user : userRepository.findAll()) {
            List<Task> allTasks = taskRepository.getAllByOwner(user);
            List<Task> finishedTasks = taskRepository.getAllByOwnerAndFinishedTimeNotNull(user);
            List<Task> unfinishedTasks = taskRepository.getAllByOwnerAndFinishedTimeNull(user);
            DaySummaryDto dayReport = new DaySummaryDto();
            dayReport.setEmail(user.getEmail());
            if (allTasks.isEmpty()) {
                continue;
            }
            if (allTasks.equals(finishedTasks)) {
                buildFinishedTasks(dayReport, finishedTasks);
            } else if (allTasks.equals(unfinishedTasks)) {
                buildUnfinishedTasks(dayReport, unfinishedTasks);
            } else {
                buildAllTaskTasks(dayReport, finishedTasks, unfinishedTasks);
            }
        }

    }

    private void buildAllTaskTasks(DaySummaryDto dayReport, List<Task> finishedTasks, List<Task> unfinishedTasks) {
        dayReport.setFinishedCount(finishedTasks.size());
        if (dayReport.getFinishedCount() > 5) {
            dayReport.setFinishedTasks(finishedTasks.subList(0, 5));
        } else {
            dayReport.setFinishedTasks(finishedTasks);
        }
        dayReport.setUnfinishedCount(unfinishedTasks.size());
        if (dayReport.getUnfinishedCount() > 5) {
            dayReport.setUnfinishedTasks(unfinishedTasks.subList(0, 5));
        } else {
            dayReport.setUnfinishedTasks(unfinishedTasks);
        }
        summaryProducer.sendAllTasks(dayReport);
    }

    private void buildUnfinishedTasks(DaySummaryDto dayReport, List<Task> unfinishedTasks) {
        dayReport.setUnfinishedCount(unfinishedTasks.size());
        if (dayReport.getUnfinishedCount() > 5) {
            dayReport.setUnfinishedTasks(unfinishedTasks.subList(0, 5));
        } else {
            dayReport.setUnfinishedTasks(unfinishedTasks);
        }
        summaryProducer.sendUnfinishedTasks(dayReport);
    }

    private void buildFinishedTasks(DaySummaryDto dayReport, List<Task> finishedTasks) {
        dayReport.setFinishedCount(finishedTasks.size());
        if (dayReport.getFinishedCount() > 5) {
            dayReport.setFinishedTasks(finishedTasks.subList(0, 5));
        } else {
            dayReport.setFinishedTasks(finishedTasks);
        }
        summaryProducer.sendFinishedTasks(dayReport);
    }
}

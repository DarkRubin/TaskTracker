package org.roadmap.tasktrackerscheduler.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.roadmap.tasktrackerscheduler.entity.Task;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DaySummaryDto  {

    String email;

    int unfinishedCount;

    int finishedCount;

    List<Task> finishedTasks;

    List<Task> unfinishedTasks;
}

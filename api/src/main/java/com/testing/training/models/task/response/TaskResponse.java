package com.testing.training.models.task.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private String id;
    private String title;
    private String description;
    private String assignee;
    private String status;
    private String priority;
    private Boolean completed;
    private String createdAt;
    private String updatedAt;
}


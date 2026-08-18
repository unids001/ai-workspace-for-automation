package com.testing.training.tasks.task;

import com.testing.training.config.RestServiceConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetTask implements Task {
    private String taskId;

    public GetTask() {
    }

    public static GetTask using(String taskId) {
        return instrumented(GetTask.class).withTaskId(taskId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (taskId == null || taskId.isBlank()) {
            throw new IllegalStateException("Task id must be provided");
        }

        actor.attemptsTo(
                Get.resource(RestServiceConfig.GET_TASK.getUrl(taskId))
        );
    }

    public GetTask withTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }
}


package com.testing.training.tasks.task;

import com.testing.training.config.RestServiceConfig;
import com.testing.training.models.task.request.CreateTaskRequest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateTask implements Task {
    private CreateTaskRequest createTaskRequest;

    public CreateTask() {
    }

    public static CreateTask using(CreateTaskRequest createTaskRequest) {
        return instrumented(CreateTask.class).withBody(createTaskRequest);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (createTaskRequest == null) {
            throw new IllegalStateException("Task request body must be provided");
        }

        actor.attemptsTo(
                Post.to(RestServiceConfig.POST_TASK.getUrl())
                        .with(request -> request
                                .contentType("application/json")
                                .body(createTaskRequest))
        );
    }

    public CreateTask withBody(CreateTaskRequest createTaskRequest) {
        this.createTaskRequest = createTaskRequest;
        return this;
    }
}


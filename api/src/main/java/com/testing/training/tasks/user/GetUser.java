package com.testing.training.tasks.user;

import com.testing.training.config.RestServiceConfig;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetUser implements Task {
    private String userId;

    public GetUser() {
    }

    public static GetUser using(String userId) {
        return instrumented(GetUser.class).withUserId(userId);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalStateException("User id must be provided");
        }

        actor.attemptsTo(
                Get.resource(RestServiceConfig.GET_USER.getUrl(userId))
        );
    }

    public GetUser withUserId(String userId) {
        this.userId = userId;
        return this;
    }
}


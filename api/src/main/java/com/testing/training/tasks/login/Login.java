package com.testing.training.tasks.login;

import com.testing.training.config.RestServiceConfig;
import com.testing.training.models.login.request.LoginRequest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Post;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Login implements Task {
    private LoginRequest loginRequest;

    public Login() {
    }

    public static Login using(LoginRequest loginRequest) {
        return instrumented(Login.class).withBody(loginRequest);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        if (loginRequest == null) {
            throw new IllegalStateException("Login request body must be provided");
        }

        actor.attemptsTo(
                Post.to(RestServiceConfig.POST_LOGIN.getUrl())
                        .with(request -> request
                                .contentType("application/json")
                                .body(loginRequest))
        );
    }

    public Login withBody(LoginRequest loginRequest) {
        this.loginRequest = loginRequest;
        return this;
    }
}

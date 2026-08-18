package com.testing.training.stepdefinitions.api;

import com.testing.training.config.MockServerManager;
import com.testing.training.models.task.request.CreateTaskRequest;
import com.testing.training.models.task.response.TaskResponse;
import com.testing.training.tasks.task.CreateTask;
import com.testing.training.tasks.task.GetTask;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.rest.SerenityRest;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;
import static net.serenitybdd.screenplay.actors.OnStage.theActorInTheSpotlight;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
public class TaskServerStepDefinitions {

    @Given("the task mock server is running")
    public void theTaskMockServerIsRunning() {
        boolean isRunning = MockServerManager.getInstance().isRunning();
        if (!isRunning) {
            throw new IllegalStateException("Mock server is not running!");
        }
        log.info("Task mock server is running on: {}", MockServerManager.getInstance().getServerUrl());
    }

    @When("I get task with id {string}")
    public void iGetTaskWithId(String taskId) {
        theActorCalled("task-" + taskId);
        theActorInTheSpotlight().attemptsTo(GetTask.using(taskId));
    }

    @When("I create task with title {string} description {string} assignee {string} status {string}")
    public void iCreateTaskWithTitleDescriptionAssigneeStatus(String title, String description, String assignee, String status) {
        theActorCalled("task-create");
        CreateTaskRequest request = CreateTaskRequest.builder()
                .title(title)
                .description(description)
                .assignee(assignee)
                .status(status)
                .build();

        theActorInTheSpotlight().attemptsTo(CreateTask.using(request));
    }

    @Then("the task response status should be {int}")
    public void theTaskResponseStatusShouldBe(int expectedStatus) {
        Response response = SerenityRest.lastResponse();
        assertThat("Unexpected status code", response.getStatusCode(), equalTo(expectedStatus));
    }

    @Then("the task response should contain id {string}")
    public void theTaskResponseShouldContainId(String expectedId) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task id in response does not match", response.getId(), equalTo(expectedId));
    }

    @Then("the task response should contain title {string}")
    public void theTaskResponseShouldContainTitle(String expectedTitle) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task title in response does not match", response.getTitle(), equalTo(expectedTitle));
    }

    @Then("the task response should contain description {string}")
    public void theTaskResponseShouldContainDescription(String expectedDescription) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task description in response does not match", response.getDescription(), equalTo(expectedDescription));
    }

    @Then("the task response should contain assignee {string}")
    public void theTaskResponseShouldContainAssignee(String expectedAssignee) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task assignee in response does not match", response.getAssignee(), equalTo(expectedAssignee));
    }

    @Then("the task response should contain status {string}")
    public void theTaskResponseShouldContainStatus(String expectedStatusValue) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task status in response does not match", response.getStatus(), equalTo(expectedStatusValue));
    }

    @Then("the task response should contain priority {string}")
    public void theTaskResponseShouldContainPriority(String expectedPriority) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task priority in response does not match", response.getPriority(), equalTo(expectedPriority));
    }

    @Then("the task response should contain completed {word}")
    public void theTaskResponseShouldContainCompleted(String expectedCompleted) {
        TaskResponse response = SerenityRest.lastResponse().as(TaskResponse.class);
        assertThat("Task completed flag in response does not match", response.getCompleted(), equalTo(Boolean.valueOf(expectedCompleted)));
    }

    @Then("the task error should be {string}")
    public void theTaskErrorShouldBe(String expectedError) {
        Response response = SerenityRest.lastResponse();
        String actualError = response.jsonPath().getString("error");
        assertThat("Task error code in response does not match", actualError, equalTo(expectedError));
    }

    @Then("the task error message should be {string}")
    public void theTaskErrorMessageShouldBe(String expectedMessage) {
        Response response = SerenityRest.lastResponse();
        String actualMessage = response.jsonPath().getString("message");
        assertThat("Task error message in response does not match", actualMessage, equalTo(expectedMessage));
    }
}


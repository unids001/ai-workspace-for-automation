package com.testing.training.stepdefinitions.api;

import com.testing.training.config.MockServerManager;
import com.testing.training.models.login.request.LoginRequest;
import com.testing.training.models.user.response.GetUserResponse;
import com.testing.training.tasks.login.Login;
import com.testing.training.tasks.user.GetUser;
import com.testing.training.testdata.manager.LocalTestData;
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

/**
 * Step definitions demonstrating WireMock mock server usage in API tests.
 *
 * @author Automation Team
 * @version 1.0
 */
@Slf4j
public class MockUserServerStepDefinitions {

    @Given("the mock server is running")
    public void theMockServerIsRunning() {
        boolean isRunning = MockServerManager.getInstance().isRunning();
        if (!isRunning) {
            throw new IllegalStateException("Mock server is not running!");
        }
        log.info("Mock server is running on: {}", MockServerManager.getInstance().getServerUrl());
    }

    @When("I login with username {string}")
    public void iLoginWithUsernameAndPassword(String userAlias) {
        theActorCalled(userAlias);
        var userData = LocalTestData.getUserDataForCurrentEnvironment(userAlias);
        LoginRequest loginRequest = LoginRequest.builder()
                .username(userData.getUsername())
                .password(userData.getPassword())
                .build();

        theActorInTheSpotlight().attemptsTo(Login.using(loginRequest));
    }

    @When("I login with credentials username {string} and password {string}")
    public void iLoginWithExplicitCredentials(String username, String password) {
        theActorCalled(username);
        LoginRequest loginRequest = LoginRequest.builder()
                .username(username)
                .password(password)
                .build();

        theActorInTheSpotlight().attemptsTo(Login.using(loginRequest));
    }

    @When("I get user with id {string}")
    public void iGetUserWithId(String userId) {
        theActorCalled("user-" + userId);
        theActorInTheSpotlight().attemptsTo(GetUser.using(userId));
    }

    @Then("the login response status should be {int}")
    public void theLoginResponseStatusShouldBe(int expectedStatus) {
        Response response = SerenityRest.lastResponse();
        assertThat("Unexpected status code", response.getStatusCode(), equalTo(expectedStatus));
    }

    @Then("the login response should contain username {string}")
    public void theLoginResponseShouldContainUsername(String expectedUsername) {
        Response response = SerenityRest.lastResponse();
        String actualUsername = response.jsonPath().getString("user.username");
        assertThat("Username in response does not match", actualUsername, equalTo(expectedUsername));
    }

    @Then("the login response should contain token_type {string}")
    public void theLoginResponseShouldContainTokenType(String expectedTokenType) {
        Response response = SerenityRest.lastResponse();
        String actualTokenType = response.jsonPath().getString("token_type");
        assertThat("Token type in response does not match", actualTokenType, equalTo(expectedTokenType));
    }

    @Then("the login error should be {string}")
    public void theLoginErrorShouldBe(String expectedError) {
        Response response = SerenityRest.lastResponse();
        String actualError = response.jsonPath().getString("error");
        assertThat("Error code in response does not match", actualError, equalTo(expectedError));
    }

    @Then("the login error message should be {string}")
    public void theLoginErrorMessageShouldBe(String expectedMessage) {
        Response response = SerenityRest.lastResponse();
        String actualMessage = response.jsonPath().getString("message");
        assertThat("Error message in response does not match", actualMessage, equalTo(expectedMessage));
    }

    @Then("the user response status should be {int}")
    public void theUserResponseStatusShouldBe(int expectedStatus) {
        Response response = SerenityRest.lastResponse();
        assertThat("Unexpected status code", response.getStatusCode(), equalTo(expectedStatus));
    }

    @Then("the user response should contain id {string}")
    public void theUserResponseShouldContainId(String expectedId) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("User id in response does not match", String.valueOf(response.getId()), equalTo(expectedId));
    }

    @Then("the user response should contain username {string}")
    public void theUserResponseShouldContainUsername(String expectedUsername) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Username in response does not match", response.getUsername(), equalTo(expectedUsername));
    }

    @Then("the user response should contain name {string}")
    public void theUserResponseShouldContainName(String expectedName) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Name in response does not match", response.getName(), equalTo(expectedName));
    }

    @Then("the user response should contain email {string}")
    public void theUserResponseShouldContainEmail(String expectedEmail) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Email in response does not match", response.getEmail(), equalTo(expectedEmail));
    }

    @Then("the user response should contain active {word}")
    public void theUserResponseShouldContainActive(String expectedActive) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Active flag in response does not match", response.getActive(), equalTo(Boolean.valueOf(expectedActive)));
    }

    @Then("the user response should contain role {string}")
    public void theUserResponseShouldContainRole(String expectedRole) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Role in response does not match", response.getRole(), equalTo(expectedRole));
    }

    @Then("the user response should contain locale {string}")
    public void theUserResponseShouldContainLocale(String expectedLocale) {
        GetUserResponse response = SerenityRest.lastResponse().as(GetUserResponse.class);
        assertThat("Locale in response does not match", response.getLocale(), equalTo(expectedLocale));
    }

}
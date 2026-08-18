package com.testing.training.stepdefinitions.commons;

import com.testing.training.config.GeneralConstants;
import com.testing.training.config.MockServerManager;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.actors.Cast;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

@Slf4j
public class Hooks {
    
    /**
     * Start WireMock server before all test scenarios
     */
    @BeforeAll
    public static void setUpWireMockServer() {
        log.info("Starting WireMock server before test suite execution");
        try {
            MockServerManager.getInstance().startServer();
            
            // Configure the API to use the mock server base URL
            String mockServerUrl = MockServerManager.getInstance().getServerUrl();
            if (mockServerUrl != null) {
                System.setProperty("mock.server.url", mockServerUrl);
                log.info("Mock server URL configured: {}", mockServerUrl);
            }
            
        } catch (Exception e) {
            log.error("Failed to start WireMock server in @BeforeAll hook", e);
            throw new RuntimeException("Failed to start WireMock server", e);
        }
    }

    @Before
    public void beforeScenario(Scenario scenario) {
        Serenity.setSessionVariable("currentScenario").to(scenario);

        String mockServerUrl = System.getProperty("mock.server.url");
        if (mockServerUrl == null || mockServerUrl.isBlank()) {
            mockServerUrl = MockServerManager.getInstance().getServerUrl();
        }

        OnStage.setTheStage(Cast.whereEveryoneCan(CallAnApi.at(mockServerUrl)));
    }
    
    /**
     * Stop WireMock server after all test scenarios
     */
    @AfterAll
    public static void tearDownWireMockServer() {
        log.info("Stopping WireMock server after test suite execution");
        try {
            MockServerManager.getInstance().stopServer();
            System.clearProperty("mock.server.url");
        } catch (Exception e) {
            log.error("Failed to stop WireMock server in @AfterAll hook", e);
        }
    }
}

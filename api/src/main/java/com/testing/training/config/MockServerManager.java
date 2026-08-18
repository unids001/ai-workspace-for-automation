package com.testing.training.config;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.MappingsLoader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

/**
 * Singleton class to manage WireMock server lifecycle for API testing.
 * Provides centralized control over mock server startup, shutdown, and configuration.
 *
 * @author Automation Team
 * @version 1.0
 */
@Slf4j
public class MockServerManager {

    private static volatile MockServerManager instance;
    private static WireMockServer wireMockServer;
    private static final int DEFAULT_PORT = 8089;
    private static final String MAPPINGS_PATH = "src/test/resources/wiremock/mappings";
    private static final String __FILES_PATH = "src/test/resources/wiremock/__files";

    private MockServerManager() {
        // Private constructor to prevent instantiation
    }

    /**
     * Get singleton instance of MockServerManager
     *
     * @return MockServerManager instance
     */
    public static MockServerManager getInstance() {
        if (instance == null) {
            synchronized (MockServerManager.class) {
                if (instance == null) {
                    instance = new MockServerManager();
                }
            }
        }
        return instance;
    }

    /**
     * Start WireMock server on default port or dynamically assigned port
     */
    public void startServer() {
        startServer(DEFAULT_PORT);
    }

    /**
     * Start WireMock server on specified port, falling back to dynamic assignment if port is busy
     *
     * @param port Preferred port for the server
     */
    public void startServer(int port) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            log.warn("WireMock server is already running on port {}", wireMockServer.port());
            return;
        }

        try {
            // Check if preferred port is available, otherwise use dynamic assignment
            int actualPort = isPortAvailable(port) ? port : findAvailablePort();

            // Create directory structure for mappings and files if not exists
            createDirectoriesIfNeeded();

            // Configure and start WireMock server
            WireMockConfiguration config = WireMockConfiguration.wireMockConfig()
                    .port(actualPort)
                    .withRootDirectory("src/test/resources/wiremock")
                    .usingFilesUnderDirectory("src/test/resources/wiremock")
                    .disableRequestJournal()
                    .containerThreads(10)
                    .jettyAcceptors(2)
                    .jettyAcceptQueueSize(100);

            wireMockServer = new WireMockServer(config);
            wireMockServer.start();

            // Configure WireMock client
            WireMock.configureFor("localhost", actualPort);

            log.info("WireMock server started successfully on port: {}", actualPort);

        } catch (Exception e) {
            log.error("Failed to start WireMock server", e);
            throw new RuntimeException("Failed to start WireMock server", e);
        }
    }

    /**
     * Stop WireMock server
     */
    public void stopServer() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            try {
                wireMockServer.stop();
                log.info("WireMock server stopped successfully on port: {}", wireMockServer.port());
            } catch (Exception e) {
                log.error("Error stopping WireMock server", e);
            }
        } else {
            log.warn("WireMock server is not running");
        }
    }

    /**
     * Reset all stub mappings and request logs
     */
    public void resetAllRequests() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.resetAll();
            wireMockServer.resetRequests();
            log.info("WireMock server reset all mappings and requests");
        } else {
            log.warn("Cannot reset: WireMock server is not running");
        }
    }

    /**
     * Reset only request logs, keeping mappings intact
     */
    public void resetRequests() {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.resetRequests();
            log.info("WireMock server reset all requests");
        } else {
            log.warn("Cannot reset requests: WireMock server is not running");
        }
    }

    /**
     * Check if WireMock server is currently running
     *
     * @return true if server is running, false otherwise
     */
    public boolean isRunning() {
        return wireMockServer != null && wireMockServer.isRunning();
    }

    /**
     * Get the port number on which WireMock server is running
     *
     * @return Port number, or -1 if server is not running
     */
    public int getPort() {
        return isRunning() ? wireMockServer.port() : -1;
    }

    /**
     * Get the base URL of the running WireMock server
     *
     * @return Base URL or null if server is not running
     */
    public String getServerUrl() {
        return isRunning() ? String.format("http://localhost:%d", wireMockServer.port()) : null;
    }

    /**
     * Create necessary directories for WireMock if they don't exist
     */
    private void createDirectoriesIfNeeded() {
        try {
            Files.createDirectories(Paths.get(MAPPINGS_PATH));
            Files.createDirectories(Paths.get(__FILES_PATH));
            log.debug("WireMock directories created/verified");
        } catch (IOException e) {
            log.error("Failed to create WireMock directories", e);
        }
    }

    /**
     * Check if a specific port is available
     *
     * @param Port number to check
     * @return true if port is available, false otherwise
     */
    private boolean isPortAvailable(int port) {
        try (ServerSocket socket = new ServerSocket(port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Find an available port dynamically
     *
     * @return An available port number
     */
    private int findAvailablePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Could not find an available port", e);
        }
    }

    /**
     * Add a simple stub for demonstration purposes
     *
     * @param urlPattern   URL pattern to match
     * @param responseBody Response body to return
     * @param statusCode   HTTP status code to return
     */
    public void addSimpleStub(String urlPattern, String responseBody, int statusCode) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stubFor(get(urlEqualTo(urlPattern))
                    .willReturn(aResponse()
                            .withStatus(statusCode)
                            .withHeader("Content-Type", "application/json")
                            .withBody(responseBody)));
            log.info("Added stub for URL pattern: {} with status: {}", urlPattern, statusCode);
        } else {
            log.warn("Cannot add stub: WireMock server is not running");
        }
    }
}
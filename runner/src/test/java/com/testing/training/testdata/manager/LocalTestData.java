package com.testing.training.testdata.manager;

import com.testing.training.testdata.manager.models.UserTestData;
import com.testing.training.testdata.manager.models.UserTestDataPerEnv;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.model.environment.EnvironmentSpecificConfiguration;
import net.thucydides.model.environment.SystemEnvironmentVariables;
import net.thucydides.model.util.EnvironmentVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class LocalTestData {
    private static final Map<String, List<UserTestData>> CACHE = new HashMap<>();
    private static final String USERS_RESOURCE_KEY = "test.data.resource.relative.file.path";
    private static final String ENVIRONMENT_KEY = "environment";
    private static final String DATA_ENV_KEY = "data.env";
    public static final EnvironmentVariables ENVIRONMENT_VARIABLES = SystemEnvironmentVariables
            .createEnvironmentVariables();

    private LocalTestData() {}

    public static String getCurrentEnv() {
        return ENVIRONMENT_VARIABLES.getProperty(ENVIRONMENT_KEY);
    }

    public static String resolveEnvironment() {
        String dataEnv = getParameter(DATA_ENV_KEY);
        log.info("Current data environment: " + dataEnv);
        return dataEnv;
    }

    public static String getParameter(String key) {
        var property = System.getProperty(key);
        if (property == null) {
            property = EnvironmentSpecificConfiguration.from(ENVIRONMENT_VARIABLES).getProperty(key);
        }
        return property;
    }

    private static List<UserTestData> loadUsersOnce() {
        String resourcePath = getParameter(USERS_RESOURCE_KEY);

        return CACHE.computeIfAbsent(resourcePath, path -> {
            log.info("Loading users from: {}", path);
            return TestDataLoader.loadUsersFromResource(path);
        });
    }

    public static UserTestDataPerEnv getUserDataForCurrentEnvironment(String userAlias) {
        String env = resolveEnvironment();
        return TestDataLoader.getUserInEnv(loadUsersOnce(), userAlias, env);
    }

    public static UserTestDataPerEnv getUserDataForEnvironment(String userAlias, String env) {
        return TestDataLoader.getUserInEnv(loadUsersOnce(), userAlias, env);
    }
}

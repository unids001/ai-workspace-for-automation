package com.testing.training.testdata.manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testing.training.testdata.manager.models.UserTestData;
import com.testing.training.testdata.manager.models.UserTestDataPerEnv;

import java.io.InputStream;
import java.util.List;

public class TestDataLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataLoader() {}

    public static List<UserTestData> loadUsersFromResource(String resourcePath) {
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return MAPPER.readValue(is, new TypeReference<List<UserTestData>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Failed to load user test data from: " + resourcePath, e);
        }
    }

    public static UserTestData findByAlias(List<UserTestData> users, String alias) {
        return users.stream()
                .filter(u -> alias.equalsIgnoreCase(u.getUserAlias()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User alias not found: " + alias));
    }

    public static UserTestDataPerEnv getUserInEnv(List<UserTestData> users, String alias, String env) {
        return findByAlias(users, alias).getByEnv(env);
    }
}

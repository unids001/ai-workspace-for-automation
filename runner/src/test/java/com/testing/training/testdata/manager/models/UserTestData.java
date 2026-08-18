package com.testing.training.testdata.manager.models;

import lombok.*;

import java.lang.reflect.Field;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTestData {
    private String userAlias;
    private UserTestDataPerEnv dev;
    private UserTestDataPerEnv qa;

    public UserTestDataPerEnv getByEnv(String env) {
        if (env == null) throw new IllegalArgumentException("Environment cannot be null");
        return switch (env.trim().toLowerCase()) {
            case "dev" -> dev;
            case "qa" -> qa;
            default -> throw new IllegalArgumentException("Unsupported environment: " + env);
        };
    }
}

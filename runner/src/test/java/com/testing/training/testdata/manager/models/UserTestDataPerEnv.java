package com.testing.training.testdata.manager.models;

import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserTestDataPerEnv {
    private String username;
    private String password;
    private Map<String, Object> customData;
}

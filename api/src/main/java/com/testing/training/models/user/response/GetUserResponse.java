package com.testing.training.models.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserResponse {
    private Long id;
    private String username;
    private String name;
    private Boolean active;
    private String email;
    private String role;
    private String locale;
    private String createdAt;
    private String updatedAt;
    private Map<String, String> metadata;
}


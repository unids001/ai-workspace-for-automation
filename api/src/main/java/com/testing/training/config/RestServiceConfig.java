package com.testing.training.config;

public enum RestServiceConfig {
    GET_USER("/api/v1/users/%s"),
    GET_TASK("/api/v1/tasks/%s"),
    POST_TASK("/api/v1/tasks"),
    POST_LOGIN("/api/v1/auth/login");

    private final String url;

    RestServiceConfig(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getUrl(Object... args) {
        return String.format(url, args);
    }
}

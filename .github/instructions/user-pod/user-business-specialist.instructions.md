---
applyTo:
  - runner/src/test/resources/features/user/**/*.feature
  - runner/src/test/java/**/tasks/**/user/**/*.java
  - runner/src/test/java/**/tasks/**/login/**/*.java
  - api/src/main/java/**/model/**/user/**/*.java
  - api/src/main/java/**/model/**/login/**/*.java
description: "Specific-use context for API test automation of the user service, including user and login-related functional flows."
---

Load and strictly apply:
`./ai/context/user-pod/user-business.md`
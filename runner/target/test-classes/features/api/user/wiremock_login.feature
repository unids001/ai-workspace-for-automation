Feature: WireMock Login API
  As a QA engineer
  I want to validate login endpoint behavior in WireMock
  So that I can confirm dynamic username templating works

  Background:
    Given the mock server is running

  @TC-001 @successfulLogin
  Scenario Outline: Successful login returns same username in response
    When I login with username "<alias>"
    Then the login response status should be 200
    And the login response should contain username "<username>"
    And the login response should contain token_type "Bearer"

    Examples:
      | alias    | username    |
      | Tyrion   | tyrion.qa   |
      | Jon Snow | jon.qa      |
      | Daenerys | daenerys.qa |

  @TC-002 @invalidLogin
  Scenario: Login fails with invalid credentials
    When I login with credentials username "invalid.user" and password "bad-password"
    Then the login response status should be 401
    And the login error should be "invalid_credentials"
    And the login error message should be "Invalid username or password"

  @TC-003 @invalidLogin
  Scenario: Login fails for locked user
    When I login with credentials username "blocked.user" and password "any-password"
    Then the login response status should be 423
    And the login error should be "user_locked"
    And the login error message should be "User account is locked"

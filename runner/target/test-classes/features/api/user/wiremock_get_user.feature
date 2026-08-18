Feature: WireMock Get User API
  As a QA engineer
  I want to validate the get user endpoint behavior in WireMock
  So that I can confirm user retrieval works as expected

  Background:
    Given the mock server is running

  @TC-004 @successfulGetUser
  Scenario Outline: Successful user retrieval returns the requested profile
    When I get user with id "<id>"
    Then the user response status should be 200
    And the user response should contain id "<id>"
    And the user response should contain username "user<id>"
    And the user response should contain name "User <id>"
    And the user response should contain email "user<id>@example.com"
    And the user response should contain active true
    And the user response should contain role "qa-tester"
    And the user response should contain locale "en-US"

    Examples:
      | id  |
      | 1   |
      | 42  |
      | 123 |

  @TC-005 @invalidGetUser
  Scenario: User retrieval fails for an invalid user id format
    When I get user with id "abc"
    Then the user response status should be 404


Feature: WireMock Task API
  As a QA engineer
  I want to validate task endpoint behavior in WireMock
  So that I can confirm task service flows work as expected

  Background:
    Given the task mock server is running

  @TC-010 @successfulGetTask
  Scenario Outline: Successful task retrieval returns the requested task
    When I get task with id "<id>"
    Then the task response status should be 200
    And the task response should contain id "<id>"
    And the task response should contain title "Task <id>"
    And the task response should contain description "Task description <id>"
    And the task response should contain assignee "qa-team"
    And the task response should contain status "OPEN"
    And the task response should contain priority "HIGH"
    And the task response should contain completed false

    Examples:
      | id  |
      | 1   |
      | 42  |
      | 123 |

  @TC-011 @successfulCreateTask
  Scenario: Successful task creation returns the created task
    When I create task with title "Review release notes" description "Validate release note content" assignee "qa-team" status "OPEN"
    Then the task response status should be 201
    And the task response should contain id "501"
    And the task response should contain title "Review release notes"
    And the task response should contain description "Validate release note content"
    And the task response should contain assignee "qa-team"
    And the task response should contain status "OPEN"
    And the task response should contain priority "MEDIUM"
    And the task response should contain completed false

  @TC-012 @invalidGetTask
  Scenario: Retrieving a missing task returns a controlled error
    When I get task with id "99999"
    Then the task response status should be 404
    And the task error should be "task_not_found"
    And the task error message should be "Task not found"


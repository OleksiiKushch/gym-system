@TrainerReport
Feature: Trainer report
  Checking the functionality of the receiving trainer report (for gym-system service only)

  Scenario: Anonymous user trying to receive trainer report
    When user sent a request for a report for the trainer with the username "Priya.Sharma"
    Then user receives error with status code 401 and messages: ""

  Scenario: User trying to receive trainer report, but it was not a trainer that was specified (some other type of user, for example, trainee)
    Given user prepares login request
      | Username | Maria.Acosta |
      | Password | 123          |
    And user sends login request
    And user should be logged in successfully
    When user sent a request for a report for the trainer with the username "Jose.Garcia"
    Then user receives error with status code 404 and messages: "Trainer with username 'Jose.Garcia' not found"
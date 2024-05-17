@OrganizeTrainingIntegrationTest
Feature: Organize training
  Checking the functionality of the training organization (integration between gym-system (main service) and trainer-manager (sub-service))

  Scenario: User organizes a new training
    Given user prepares login request
      | Username | Maria.Acosta |
      | Password | 123          |
    And user sends login request
    And user should be logged in successfully
    And user prepares training organization request
      | Trainee Username | Jose.Garcia       |
      | Trainer Username | Maria.Acosta      |
      | Training Name    | Intense dead-lift |
      | Training Type    | STRENGTH          |
      | Training Date    | 2024-03-18        |
      | Duration         | 65                |
    When user sends a request to organize a new training
    Then wait for 1000 milliseconds
    And should successfully create a new training session with name "Intense dead-lift"
    And all training required fields are present
    And user sent a request for a report for the trainer with the username "Maria.Acosta"
    And check the report for the trainer, it should be: '{"trainerUsername":"Maria.Acosta","trainerFirstName":"Maria","trainerLastName":"Acosta","history":{"2024":{"MARCH":65}},"active":true}'
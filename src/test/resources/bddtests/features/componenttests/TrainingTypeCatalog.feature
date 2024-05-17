@TrainingTypeCatalog
Feature: Training type catalog
  Checking the functionality of retrieving existing training types in the system

  Scenario: User retrieve existing training types
    Given user prepares login request
      | Username | Jose.Garcia |
      | Password | 123         |
    And user sends login request
    And user should be logged in successfully
    When user sends a request to retrieve existing training types
    Then user get the following training types: '[{"id":1,"name":"CARDIO"},{"id":2,"name":"STRENGTH"},{"id":3,"name":"BOXING"},{"id":4,"name":"YOGA"}]'

  Scenario: Anonymous user try to retrieve existing training types
    When user sends a request to retrieve existing training types
    Then user receives error with status code 401 and messages: ""
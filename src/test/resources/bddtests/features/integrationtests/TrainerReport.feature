@TrainerReportIntegrationTest
Feature: Trainer report
  Checking the functionality of the receiving trainer report (integration between gym-system (main service) and trainer-manager (sub-service))

  Scenario Outline: : User trying to receive trainer report
    Given user prepares login request
      | Username | Maria.Acosta |
      | Password | 123          |
    And user sends login request
    And user should be logged in successfully
    When user sent a request for a report for the trainer with the username "<Username>"
    And check the report for the trainer, it should be: '<Report>'

    Examples:
      | Username     | Report                                                                                                                                                         |
      | Priya.Sharma | {"trainerUsername":"Priya.Sharma","trainerFirstName":"Priya","trainerLastName":"Sharma","history":{"2024":{"MAY":90}},"active":true}                           |
      | Rajesh.Kumar | {"trainerUsername":"Rajesh.Kumar","trainerFirstName":"Rajesh","trainerLastName":"Kumar","history":{"2023":{"DECEMBER":135},"2024":{"APRIL":45}},"active":true} |
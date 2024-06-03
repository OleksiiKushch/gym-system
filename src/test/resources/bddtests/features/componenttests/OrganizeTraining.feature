@OrganizeTraining
Feature: Organize training
  Checking the functionality of the training organization (for gym-system service only)

  Scenario: Anonymous user try to organizes a new training
    Given user prepares training organization request
      | Trainee Username | Jose.Garcia       |
      | Trainer Username | Maria.Acosta      |
      | Training Name    | Intense dead-lift |
      | Training Type    | STRENGTH          |
      | Training Date    | 2024-03-18        |
      | Duration         | 65                |
    When user sends a request to organize a new training
    Then user receives error with status code 401 and messages: ""

  Scenario Outline: Organize a new training but some required fields are empty
    Given user prepares login request
      | Username | Maria.Acosta |
      | Password | 123          |
    And user sends login request
    And user should be logged in successfully
    And user prepares training organization request
      | Trainee Username | <Trainee Username> |
      | Trainer Username | <Trainer Username> |
      | Training Name    | <Training Name>    |
      | Training Type    | <Training Type>    |
      | Training Date    | <Training Date>    |
      | Duration         | <Duration>         |
    When user sends a request to organize a new training
    Then user receives error with status code 400 and messages: "<Error codes>"

    Examples:
      | Trainee Username | Trainer Username | Training Name     | Training Type | Training Date | Duration | Error codes                                                                                                               |
      |                  | Maria.Acosta     | Intense dead-lift | STRENGTH      | 2024-03-18    | 65       | NotEmpty.createTrainingForm.traineeUsername                                                                               |
      | Jose.Garcia      |                  | Intense dead-lift | STRENGTH      | 2024-03-18    | 65       | NotEmpty.createTrainingForm.trainerUsername                                                                               |
      | Jose.Garcia      | Maria.Acosta     |                   | STRENGTH      | 2024-03-18    | 65       | NotEmpty.createTrainingForm.trainingName                                                                                  |
      | Jose.Garcia      | Maria.Acosta     | Intense dead-lift |               | 2024-03-18    | 65       | NotEmpty.createTrainingForm.trainingType                                                                                  |
      | Jose.Garcia      | Maria.Acosta     | Intense dead-lift | STRENGTH      |               | 65       | NotEmpty.createTrainingForm.trainingDate                                                                                  |
      | Jose.Garcia      | Maria.Acosta     | Intense dead-lift | STRENGTH      | 2024-03-18    |          | NotEmpty.createTrainingForm.duration                                                                                      |
      |                  | Maria.Acosta     |                   | STRENGTH      | 2024-03-18    |          | NotEmpty.createTrainingForm.traineeUsername~NotEmpty.createTrainingForm.trainingName~NotEmpty.createTrainingForm.duration |

  Scenario Outline: Organize a new training but field "Training Date" is invalid
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
      | Training Date    | <Training Date>   |
      | Duration         | 65                |
    When user sends a request to organize a new training
    Then user receives error with status code 400 and messages: "<Error messages>"

    Examples:
      | Training Date | Error messages                                                                                                          |
      | 2024-13-18    | The value '2024-13-18' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |
      | 2024-03-32    | The value '2024-03-32' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |
      | 2022-02-29    | Text '2022-02-29' could not be parsed: Invalid date 'February 29' as '2022' is not a leap year                          |
      | 18/3/2024     | The value '18/3/2024' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'.  |
      | 1710768615    | The value '1710768615' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |

  Scenario: Organize a new training but field "Training Type" is invalid
    Given user prepares login request
      | Username | Maria.Acosta |
      | Password | 123          |
    And user sends login request
    And user should be logged in successfully
    And user prepares training organization request
      | Trainee Username | Jose.Garcia                |
      | Trainer Username | Maria.Acosta               |
      | Training Name    | Intense dead-lift          |
      | Training Type    | NON-EXISTENT TRAINING_TYPE |
      | Training Date    | 2024-03-18                 |
      | Duration         | 65                         |
    When user sends a request to organize a new training
    Then user receives error with status code 400 and messages: "The value 'NON-EXISTENT TRAINING_TYPE' is not a valid Training Type. Valid Training Type values are: BOXING, CARDIO, YOGA, STRENGTH."
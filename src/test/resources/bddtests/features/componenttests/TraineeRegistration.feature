@TraineeRegistration
Feature: Trainee registration
  Checking the functionality of trainee registration

  Scenario: Registration of new trainee
    Given trainee prepares registration request
      | First Name       | James                  |
      | Last Name        | Smith                  |
      | Date of Birthday | 2010-06-03             |
      | Address          | Toronto, Elm street, 3 |
    When trainee sends registration request
    Then trainee with the username "James.Smith" has successfully registered
    And all trainee required fields are present
    And some trainee optional fields are present and next are not: ""

  Scenario Outline: Registration of new trainee if some optional fields are empty
    Given trainee prepares registration request
      | First Name       | Team            |
      | Last Name        | Jasper          |
      | Date of Birthday | <Date of Birth> |
      | Address          | <Address>       |
    When trainee sends registration request
    Then trainee with the username "<Username>" has successfully registered
    And all trainee required fields are present
    And some trainee optional fields are present and next are not: "<Not provided optional fields>"

    Examples:
      | Username     | Date of Birth | Address                | Not provided optional fields |
      | Team.Jasper  |               | Toronto, Elm street, 3 | Date of Birthday             |
      | Team.Jasper1 | 2010-06-03    |                        | Address                      |
      | Team.Jasper2 |               |                        | Date of Birthday~Address     |

  Scenario Outline: Registration of new trainee but some required fields are empty
    Given trainee prepares registration request
      | First Name       | <First Name>           |
      | Last Name        | <Last Name>            |
      | Date of Birthday | 2010-06-03             |
      | Address          | Toronto, Elm street, 3 |
    When trainee sends registration request
    Then user receives error with status code 400 and messages: "<Error codes>"

    Examples:
      | First Name | Last Name | Error codes                                                                          |
      |            | Lawrence  | NotEmpty.registrationTraineeForm.firstName                                           |
      | Alex       |           | NotEmpty.registrationTraineeForm.lastName                                            |
      |            |           | NotEmpty.registrationTraineeForm.lastName~NotEmpty.registrationTraineeForm.firstName |

  Scenario Outline: Registration of new trainee but field "Date of Birth" is invalid
    Given trainee prepares registration request
      | First Name       | Alex                   |
      | Last Name        | Lawrence               |
      | Date of Birthday | <Date of Birth>        |
      | Address          | Toronto, Elm street, 3 |
    When trainee sends registration request
    Then user receives error with status code 400 and messages: "<Error messages>"

    Examples:
      | Date of Birth | Error messages                                                                                                          |
      | 2010-13-03    | The value '2010-13-03' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |
      | 2010-06-32    | The value '2010-06-32' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |
      | 2010-02-29    | Text '2010-02-29' could not be parsed: Invalid date 'February 29' as '2010' is not a leap year                          |
      | 3/6/2010      | The value '3/6/2010' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'.   |
      | 1275571830    | The value '1275571830' does not match the expected date format. Please ensure the date follows the format 'yyyy-MM-dd'. |
@UserLogin
Feature: User login
  Checking the functionality of user login

  Scenario: User login
    Given user prepares login request
      | Username | Jose.Garcia |
      | Password | 123         |
    When user sends login request
    Then user should be logged in successfully

  @RestartLoginBruteforceProtection
  Scenario Outline: User login but bad credentials
    Given user prepares login request
      | Username | <Username> |
      | Password | <Password> |
    When user sends login request
    Then user receives error with status code 400 and messages: "Bad credentials"

    Examples:
      | Username          | Password           |
      | Jose.Garcia       | incorrect_password |
      | non-existent.user | 123                |

  Scenario Outline: User login but some required fields are empty
    Given user prepares login request
      | Username | <Username> |
      | Password | <Password> |
    When user sends login request
    Then user receives error with status code 400 and messages: "<Error codes>"

    Examples:
      | Username    | Password | Error codes                                            |
      | Jose.Garcia |          | NotEmpty.loginForm.password                            |
      |             | 123      | NotEmpty.loginForm.username                            |
      |             |          | NotEmpty.loginForm.password~otEmpty.loginForm.username |

  @RestartLoginBruteforceProtection
  Scenario: User triger bruteforce protection on login
    Given user prepares login request
      | Username | Jose.Garcia        |
      | Password | incorrect_password |
    When user sends login request
    And user sends login request
    And user sends login request
    And user sends login request
    Then user receives error with status code 429 and messages: "Too many requests. Please try again later."
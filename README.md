# gym-system

## Run cucumber tests
[Cucumber official Doc](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-spring)
<br>
[Additional Doc for Cucumber + Spring](https://github.com/cucumber/cucumber-jvm/tree/main/cucumber-spring)

1) Run all cucumber tests: 
Component tests: mvn test -Dtest=org.example.bddtests.RunCucumberComponentTests
Integration tests: mvn test -Dtest=org.example.bddtests.RunCucumberIntegrationTests
2) To run specific cucumber tests, for example, you can do so using tags. Just add the necessary tag with the following property to the command: -Dcucumber.filter.tags="@Tag"
<br>Examples:<br>
mvn test -Dtest=org.example.bddtests.RunCucumberComponentTests -Dcucumber.filter.tags="@TraineeRegistration"
<br>Also, you can combine tags using logical operators such as "and", "or", and "not".<br>
For example:<br>
-Dcucumber.filter.tags="@Tag1 and @Tag2" <br>
-Dcucumber.filter.tags="@Tag1 or @Tag2" <br>
-Dcucumber.filter.tags="not @Tag" <br>

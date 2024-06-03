package org.example.bddtests.steps.general;

import io.cucumber.java.en.Then;

public class WaitHelperStepDef {

    @Then("wait for {int} milliseconds")
    public void waitForMilliseconds(int milliseconds) throws InterruptedException {
        Thread.sleep(milliseconds);
    }
}

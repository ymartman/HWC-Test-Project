package com.libertex.qa.hello;

import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.challenge.logic.ClientOperations;
import com.libertex.qa.challenge.api.model.response.HelloData;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.libertex.qa.core.reporting.Steps.info;

public class ValidHelloTest {
    private String user = RandomStringUtils.randomAlphabetic(8);
    private String fullName = RandomStringUtils.randomAlphabetic(8);

    /**
     * Test Case:
     * Preconditions:
     * 1)Create user
     * Steps:
     * 1)Login with user
     * 2)Send hello request with xCode from login
     * 3)Verify message has format "Hello 'fullName'!
     */
    @Test
    @Description("Assert that logged user can see hello")
    public void validHello(){
        //Login
        String xCode = new ClientOperations().login(user);

        //Check world for the hello
        info("Get Hello for user " + user);
        HelloData response = CallExecutor
                .execute(
                        new ChallengeService()
                                .setXSessionId(xCode)
                                .getService()
                                .hello());

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getResultCode(), "Ok", "Unexpected ResultCode");
        softAssert.assertEquals(response.getMessage(), String.format("Hello, %s!",fullName), "Unexpected hello message");
        softAssert.assertAll();
    }

    @BeforeMethod
    public void preconditions(){
        new ClientOperations().create(user, fullName);
    }

    @AfterMethod
    public void postConditions(){
        new ClientOperations().logout(user);
    }
}

package com.libertex.qa.hello;

import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.challenge.logic.ClientOperations;
import com.libertex.qa.challenge.api.model.response.HelloData;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;

import static com.libertex.qa.core.reporting.Steps.info;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

public class MultipleLoggedHelloTest {
    private HashMap<String, String> users = new HashMap<>();

    //Кстати BDD для таких кейсов не очень выглядит т.к. приходится изголяться при шаринге ресурсов
    /**
     * Test Case:
     * 1)Create N clients
     * 2)Login with all N clients
     * 3)Take hello message for every clients
     * 4)Verify
     */
    @Test(priority = 10000)
    @Description("Assert multiple logged clients")
    public void multipleLoggedClientsHello(){
        //В теориии здесь можно было бы использовать фьючеры или просто из какого-нибудь тред пула наспавнить потоков,
        //   но это в случае если минуты жалко

        //Login
        HashMap<String, String> xCodes = new HashMap<>();
        users.forEach( (key, value) ->
                xCodes.put(value, new ClientOperations().login(key)) );

        users.forEach( (key, value) ->
                assertHello(xCodes.get(value), key, value) );
    }

    /**
     * Assert rightness of hello world power
     */
    private void assertHello(String xCode, String user, String fullName){
        //Check world for the hello
        info("Get Hello for user " + user);
        HelloData response = CallExecutor
                .execute(
                        new ChallengeService()
                                .setXSessionId(xCode)
                                .getService()
                                .hello(),
                        200,
                        "OK"
                );

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(response.getResultCode(), "Ok", "Unexpected ResultCode");
        softAssert.assertEquals(response.getMessage(), String.format("Hello, %s!",fullName), "Unexpected hello message");
        softAssert.assertAll();
    }

    @BeforeMethod
    public void preconditions(){
        //На таком количестве у меня локально всё свалилось
        int stressCount = 10000;
        for (int i=0; i<stressCount; i++){
            users.put(randomAlphabetic(12),randomAlphabetic(12));
        }
        users.forEach( (key, value) ->
            new ClientOperations().create(key, value)
        );
    }

    @AfterMethod
    public void postConditions(){
        users.forEach( (key, value) ->
                new ClientOperations().logout(key)
        );
    }
}

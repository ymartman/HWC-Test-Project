package com.libertex.qa.other;

import com.libertex.qa.challenge.api.model.response.ClientsData;
import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.libertex.qa.core.reporting.Steps.info;

public class GetClientsTest {

    /**
     * Test Case:
     * 1)Send get request on challenge/clients endpoint
     * 2)Ensure that http status is 200 and resultCode is ok
     * 3)Check model is right
     */
    @Test
    @Description("Check that get clients is working")
    private void getClients(){

        //Execute GET clients
        info("Execute GET clients");
        ClientsData clientsData = CallExecutor
                .execute(
                        new ChallengeService().getService().getClients());

        //Assert ResultCode
        info("Assertions:");
        Assert.assertEquals(
                clientsData.getResultCode(),
                "Ok",
                "Result code in body is invalid");
        Assert.assertNotNull(
                clientsData.getClients(),
                "List of clients not returned");
    }
}

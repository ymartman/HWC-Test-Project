package com.libertex.qa.other;

import com.libertex.qa.challenge.api.model.ClientData;
import com.libertex.qa.challenge.api.model.response.ClientsData;
import com.libertex.qa.challenge.api.model.response.GeneralData;
import com.libertex.qa.challenge.api.view.ChallengeService;
import com.libertex.qa.core.api.CallExecutor;
import io.qameta.allure.Description;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static com.libertex.qa.core.reporting.Steps.info;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;

public class PostValidClientsTest {

    @DataProvider(name = "data-provider")
    public Object[][] dataProviderMethod() {
        int max = 255;
        int min = 1;
        return new Object[][] {
                //Среднее
                { randomAlphabetic(8), randomAlphabetic(8)},
                //Максимальные границы
                { randomAlphabetic(max), randomAlphabetic(8)},
                { randomAlphabetic(8), randomAlphabetic(max)},
                { randomAlphabetic(max), randomAlphabetic(max)},
                //Минимальные границы
                { randomAlphabetic(min), randomAlphabetic(8)},
                { randomAlphabetic(8), randomAlphabetic(min)},
                { randomAlphabetic(min), randomAlphabetic(min)},
                 };
    }

    @Test(dataProvider = "data-provider")
    @Description("Check post clients")
    public void postValidClients(String fullName, String username){
        info("POST new client [" + fullName + "/" + username + "]");
        GeneralData response = CallExecutor
                .execute(new ChallengeService().getService().postClients(
                        new ClientData()
                            .setFullName(fullName)
                            .setUsername(username)
                ));

        //Assert ResultCode
        info("Assertions");
        assertEquals(
                response.getResultCode(),
                "Ok",
                "Result code in body is invalid");

        //Execute GET clients
        info("Check client created with GET clients:");
        ClientsData clients = CallExecutor
                .execute(
                        new ChallengeService().getService().getClients());

        //Assert that exist in database
        info("Assertions");
        assertEquals(
                clients.getResultCode(),
                "Ok",
                "Result code in body is invalid");
        assertNotNull(
                clients.getClients(),
                "List of clients not returned");
        assertTrue(
                clients.getClients().stream().anyMatch(s -> s.equals(username)),
                "Newly created user '" + fullName + "not found");
    }
}

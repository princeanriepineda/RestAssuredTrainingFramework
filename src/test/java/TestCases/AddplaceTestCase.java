package TestCases;


import Base.BaseRSA;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public class AddplaceTestCase extends BaseRSA {

    // Define the file paths relative to the current working directory
    String addPlaceJsonPath = System.getProperty("user.dir") + "/src/main/java/TestDataRequestJson/addPlace.json";
    String expectedResponseJsonPath = System.getProperty("user.dir") + "/src/main/java/TestDataResponseJson/addPlaceResponse.json";

    @Test
    @Feature("Add Place API")
    @Description("Test to validate the add place API in RSA")
    @Step("Make the POST request and validate the response")
    public void testAddPlace() throws IOException {


        // Make the POST request and get the response
        String responseString = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(addPlaceJsonPath))))
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200)
                .header("server", "Apache/2.4.52 (Ubuntu)") // Assertion for server header
                .extract().response().asString();

        // Get and print the status code
        int statusCode = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(addPlaceJsonPath))))
                .when().post("maps/api/place/add/json")
                .then().extract().response().statusCode();

        // Check if the response string is not null using Hamcrest Matchers
        assertThat(responseString, notNullValue());

        // Print the raw response
        System.out.println("Response: " + responseString);

        // Read the expected response JSON from file
        String expectedResponseString = new String(Files.readAllBytes(Paths.get(expectedResponseJsonPath)));

        // Parse the actual and expected response JSON
        JSONObject actualResponseJson = new JSONObject(responseString);
        JSONObject expectedResponseJson = new JSONObject(expectedResponseString);

        // Extract the static values (status and scope) from the actual response
        String actualStatus = actualResponseJson.getString("status");
        String actualScope = actualResponseJson.getString("scope");

        // Expected static values
        String expectedStatus = expectedResponseJson.getString("status");
        String expectedScope = expectedResponseJson.getString("scope");

        // Validate the static fields
        Assert.assertEquals(actualStatus, expectedStatus, "Status mismatch");
        Assert.assertEquals(actualScope, expectedScope, "Scope mismatch");

        // Logging a step in Allure report
        Allure.step("Response: " + responseString);

        // Logging a step in Allure report
        Allure.step("Status Code: "+statusCode);

    }

}

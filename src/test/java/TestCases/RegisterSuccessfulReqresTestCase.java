package TestCases;

import Base.BaseReqres;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RegisterSuccessfulReqresTestCase extends BaseReqres {
    // Define the file paths relative to the current working directory
    String RegisterSuccessfulJsonPath = System.getProperty("user.dir") + "/src/main/java/TestDataRequestJson/RegisterSuccessfulReqres.json";
    String expectedResponseJsonPath = System.getProperty("user.dir") + "/src/main/java/TestDataResponseJson/RegisterSuccessfulReqresResponse.json";


    @Test
    @Feature("Register API")
    @Description("Test to validate registering a user successfully")
    @Step("Make a POST request to register a user and validate the response")
    public void testPostRequest() throws IOException {
        String responseString =  given().log().all()
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(RegisterSuccessfulJsonPath))))
                .when().post("api/register")
                .then().log().all().assertThat().statusCode(200)
                .extract().response().asString();

        // Get and print the status code
        int statusCode = given().log().all()
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(RegisterSuccessfulJsonPath))))
                .when().post("api/register")
                .then().log().all().extract().response().statusCode();

        // Read the expected response JSON from file
        String expectedResponseString = new String(Files.readAllBytes(Paths.get(expectedResponseJsonPath)));

        // Convert the response strings to JSON objects
        JSONObject actualResponseJson = new JSONObject(responseString);
        JSONObject expectedResponseJson = new JSONObject(expectedResponseString);

        // Compare the JSON objects
        Assert.assertEquals(actualResponseJson.toString(), expectedResponseJson.toString(), "Response MISMATCH. Expected Response: " + expectedResponseJson + " Actual: " + actualResponseJson);

        // Logging a step in Allure report
        Allure.step("Response: " + responseString);

        // Logging a step in Allure report
        Allure.step("Status Code: "+statusCode);
    }
}

package TestCases;
import Base.BaseRSA;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
public class UpdateplaceTestCase extends BaseRSA {
    String addPlaceJsonPath = System.getProperty("user.dir") + "/src/main/java/TestDataRequestJson/addPlace.json";
    String placeID;
    //String placeID="e5fff58dc83a9c4fef32d08388aac915";
    String newAddress = "80 Autumn Walk, Australia";

    @Test(priority = 1)
    @Feature("Add Place API")
    @Description("Test to validate adding a place and retrieving the place ID")
    @Step("Make a POST request to add a place and validate the response")
    public void testAddPlace() throws IOException {
        String AddPlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(new String(Files.readAllBytes(Paths.get(addPlaceJsonPath))))
                .when().post("maps/api/place/add/json")
                .then().log().all().statusCode(200)
                .header("server", "Apache/2.4.52 (Ubuntu)")
                .extract().response().asString();

        System.out.println("Response: " + AddPlaceResponse);

        JsonPath js = new JsonPath(AddPlaceResponse);
        placeID = js.getString("place_id");
        Assert.assertNotNull(placeID, "Place ID is null");

        // Logging a step in Allure report
        Allure.step("Response: " + AddPlaceResponse);
    }

    @Test(priority = 2)
    @Feature("Update Place API")
    @Description("Test to validate updating a place address")
    @Step("Update the place address using PUT request and validate the response")
    public void testUpdatePlace() {

        String requestBody = "{\n" +
                "  \"place_id\": \"" + placeID + "\",\n" +
                "  \"address\": \"" + newAddress + "\",\n" +
                "  \"key\": \"qaclick123\"\n" +
                "}\n";

        String UpdatePlaceResponse = given().log().all()
                .queryParam("key", "qaclick123")
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when().put("maps/api/place/update/json")
                .then().log().all().statusCode(200)
                .body("msg", equalTo("Address successfully updated"))
                .extract().response().asString();

        // Logging a step in Allure report
        Allure.step("Response: " +  UpdatePlaceResponse);
    }

}

package Base;

import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseRSA {

    Properties prop;


    @BeforeClass
    public void setup() throws IOException {

        // Load global.properties file
        prop = new Properties();
        FileInputStream fis = new FileInputStream(System.getProperty("user.dir") +"/src/main/java/Global/Global.properties");
        prop.load(fis);

        // Set base URI from global.properties
        RestAssured.baseURI = prop.getProperty("baseURI");
    }
    @AfterClass
    public void quit(){
        RestAssured.reset();
    }
}

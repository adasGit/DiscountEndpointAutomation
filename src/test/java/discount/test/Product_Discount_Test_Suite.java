package discount.test;

import discount.lib.Discount;
import discount.lib.DiscountWrapper;
import discount.lib.Token;
import discount.lib.Utils;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;

import io.restassured.exception.PathException;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * The test suite
 */
public class Product_Discount_Test_Suite {
    private static Logger logger = LoggerFactory.getLogger(Product_Discount_Test_Suite.class);
    private Utils utils;
    private Token token;

    Map<String,String> dictionary = new HashMap<>();
    ArrayList<String> keys = new ArrayList<>();
    int current = 0;
    int extra = 0;

    private void setBasicUrl() {
        RestAssured.reset();
        RestAssured.baseURI = "https://products.izettletest.com";
        RestAssured.basePath = "/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828";
    }

    /**
     * Insert test data
     */
    private void insertTestData() {
        Discount discount;
        String uuid;

        logger.info("Inserting test data ...");
        setBasicUrl();
        for (int i=0; i<3; i++) {
            discount = new Discount();
            uuid = utils.generateUUID();
            discount.setUuid(uuid);
            discount.setName(utils.generateString());
            discount.setDescription("");
            discount.setImageLookupKeys(Collections.<String>emptyList());
            if(i%2 == 1) discount.setAmount(utils.generateDigits(), "SEK");
            if(i%2 == 0) discount.setPercentage("100");
            discount.setExternalReference(utils.generateString());

            String location = given()
                    .header("Authorization", "Bearer " + token.getAccessToken())
                    .contentType(ContentType.JSON)
                    .body(discount)
                    .when()
                    .post("/discounts")
                    .then().statusCode(201).extract().header("Location");

            extra++;
            dictionary.put(uuid, location);
            keys.add(uuid);
        }
    }

    /**
     * Count current discount entities
     */
    private int countEntities() {
        int items = 0;
        setBasicUrl();
        try {
            ArrayList<String> res = given()
                    .header("Authorization", "Bearer " + token.getAccessToken())
                    .when()
                    .get("/discounts")
                    .then()
                    .extract().path("uuid");
            items = res.size();
        }catch (PathException ex) {
            logger.info(ex.getMessage());
        }
        return items;
    }

    @BeforeClass
    public void setUpClass() {
        logger.info("Setting up test suite ...");
        BasicConfigurator.configure();
        setBasicUrl();
        utils = new Utils();
        token = new Token();
        token.verifyToken();
        current = countEntities();
        insertTestData();
    }

    @AfterClass
    public void tearDownClass() {
        logger.info("Cleaning up test data ...");
        try {
            for (int i=0; i<keys.size(); i++) {
                given()
                        .header("Authorization", "Bearer " + token.getAccessToken())
                        .pathParam("uuid", keys.get(i))
                        .when()
                        .delete("/discounts/{uuid}")
                        .then().statusCode(204);
            }
        }catch (PathException ex) {
            logger.info(ex.getMessage());
        }
    }


    @Test(priority = 1)
    public void test_Retrieve_All_Discounts(Method method) {
        logger.info("Test Name: " + method.getName());
        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .when()
                .get("/discounts")
                .then().statusCode(200);

        ArrayList<String> res = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .when()
                .get("/discounts")
                .then()
                .extract().path("uuid");

        Assert.assertEquals(res.size(), current+extra, "Failed to retrieve all discounts!");
    }

    @Test(priority = 2)
    public void test_Retrieve_Single_Discount(Method method) {
        logger.info("Test Name: " + method.getName());
        String value = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .pathParam("uuid", keys.get(0))
                .when()
                .get("/discounts/{uuid}")
                .then()
                .extract().path("percentage");

        Assert.assertEquals(value, "100", "Failed to retrieve a particular discount!");
    }

    @Test(priority = 3)
    public void test_Invalid_Token_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .header("Authorization", "Bearer eyJraWQiOiIxNDkwNzExMTIwMTQ1IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYX")
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.header("WWW-Authenticate").contains("The provided access token is invalid!"));
    }

    @Test(priority = 4)
    public void test_Expired_Token_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .header("Authorization", "Bearer " + token.getExpiredAccessToken())
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.header("WWW-Authenticate").contains("ACCESS_TOKEN_EXPIRED"));
    }

    @Test(priority = 5)
    public void test_Credentials_Required_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.asString().contains("Credentials are required"));
    }

    @Test(priority = 6)
    public void test_Wrong_Credential_Response_Code(Method method) {
        logger.info("Test Name: " + method.getName());
        given()
                .header("Authorization", "Bearer " + token.getInvalidAccessToken())
                .when()
                .get("/discounts")
                .then().statusCode(401);
    }

    @Test(priority = 11)
    public void test_Create_Single_Discount_Location(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(utils.generateUUID());
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount();
        discount.setPercentage("25");
        discount.setExternalReference(utils.generateString());

        String location = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .when()
                .post("/discounts")
                .then()
                .extract().header("Location");

        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .when()
                .get(location)
                .then().statusCode(200);
    }

    /**
     * HTTP/1.1 400 Bad Request
     * "developerMessage":"Missing required creator property 'uuid' (index 0)"
     */
    @Test(enabled = false)
    public void test_Create_Multiple_Discount_Entity_At_Once(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount;
        ArrayList<Discount> discounts = new ArrayList<>();

        for (int i=0; i<5; i++) {
            discount = new Discount();
            discount.setUuid(utils.generateUUID());
            discount.setName(utils.generateString());
            discount.setDescription("");
            discount.setImageLookupKeys(Collections.<String>emptyList());
            discount.setAmount(utils.generateDigits(), "SEK");
            discount.setPercentage();
            discount.setExternalReference(utils.generateString());

            discounts.add(discount);
        }

        DiscountWrapper discountWrapper = new DiscountWrapper(discounts);
        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discountWrapper)
                .when()
                .post("/discounts")
                .then().statusCode(201);
    }

    @Test(priority = 10)
    public void test_Create_Duplicate_Discount_Entity(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(keys.get(0));
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount();
        discount.setPercentage("25");
        discount.setExternalReference(utils.generateString());

        int count_before_post = countEntities();

        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .when()
                .post("/discounts");

        int count_after_post = countEntities();

        Assert.assertEquals(count_before_post, count_after_post, "Should not create a duplicate entity!");
    }

    @Test(priority = 7)
    public void test_Successful_Update_Of_Discount_Entity(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(keys.get(1));
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount(100, "USD");
        discount.setPercentage();
        discount.setExternalReference(utils.generateString());

        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .pathParam("uuid", keys.get(1))
                .when()
                .put("/discounts/{uuid}")
                .then().statusCode(204);
    }

    /**
     * "propertyName": "percentage"
     * "developerMessage": "must be less than or equal to 100"
     */
    @Test(priority = 8)
    public void test_Percentage_Constraint_Violations(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(utils.generateUUID());
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount();
        discount.setPercentage("110");
        discount.setExternalReference(utils.generateString());

        String res = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .when()
                .post("/discounts")
                .then()
                .statusCode(422)
                .extract()
                .response().path("developerMessage");

        Assert.assertTrue(res.contains("constraint violations"), "Failed to restrict 'Percentage' constraint violation!!");
    }

    /**
     * "propertyName": "specifiedAmountOrPercentage"
     * "developerMessage": "Discounts must have either amount or percentage"
     */
    @Test(priority = 9)
    public void test_Create_Discount_Entity_Using_Both_Percentage_And_Amount(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(utils.generateUUID());
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount(utils.generateDigits(), "USD");
        discount.setPercentage("20");
        discount.setExternalReference(utils.generateString());

        String res = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .when()
                .post("/discounts")
                .then()
                .statusCode(422)
                .extract()
                .response().path("developerMessage");

        Assert.assertTrue(res.contains("constraint violations"), "Failed to restrict Constraint Violation!!");
    }

}

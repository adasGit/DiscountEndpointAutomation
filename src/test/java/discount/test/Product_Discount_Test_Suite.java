package discount.test;

import discount.lib.Discount;
import discount.lib.DiscountWrapper;
import discount.lib.Token;
import discount.lib.Utils;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
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


/**
 * The test suite
 */
public class Product_Discount_Test_Suite {
    private static Logger logger = LoggerFactory.getLogger(Product_Discount_Test_Suite.class);
    private Utils utils;
    private Token token;

    private void insertTestData() {
        //ToDo: Insert test data
    }

    private int countEntities() {
        return 0;
    }

    @BeforeClass
    public void setUpClass() {
        BasicConfigurator.configure();
        utils = new Utils();
        token = new Token();
    }

    @AfterClass
    public void tearDownClass() {
        //ToDo: Clean all the test data
    }

    @BeforeMethod
    public void setUp() {
        token.verifyToken();
        RestAssured.reset();
        RestAssured.baseURI = "https://products.izettletest.com";
        RestAssured.basePath = "/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828";
    }


    @Test
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

        Assert.assertEquals(res.size(), 167, "Failed to retrieve all discounts!");
    }

    @Test
    public void test_Retrieve_Single_Discount(Method method) {
        logger.info("Test Name: " + method.getName());
        ArrayList<String> res = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .when()
                .get("/discounts")
                .then()
                .extract().path("uuid");

        String value = given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .pathParam("uuid", res.get(0))
                .when()
                .get("/discounts/{uuid}")
                .then()
                .extract().path("percentage");

        Assert.assertEquals(value, "100", "Failed to retrieve a particular discount!");
    }


    @Test
    public void test_Token_Signature_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .header("Authorization", "Bearer " + token.getInvalidAccessToken())
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.header("WWW-Authenticate").contains("Could not verify signature!"));
    }

    @Test
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

    @Test
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

    @Test
    public void test_Credentials_Required_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.asString().contains("Credentials are required"));
    }

    @Test
    public void test_Wrong_Credential_Response_Code(Method method) {
        logger.info("Test Name: " + method.getName());
        given()
                .header("Authorization", "Bearer " + token.getInvalidAccessToken())
                .when()
                .get("/discounts")
                .then().statusCode(401);
    }

    @Test
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
    @Test
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
            discount.setAmount(200+i, "SEK");
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

    @Test
    public void test_Create_Duplicate_Discount_Entity(Method method) {
        logger.info("Test Name: " + method.getName());

    }

    /**
     * "propertyName": "percentage"
     * "developerMessage": "must be less than or equal to 100"
     */
    @Test
    public void test_Percentage_Constraint_Violations(Method method) {

    }

    /**
     * "propertyName": "specifiedAmountOrPercentage"
     * "developerMessage": "Discounts must have either amount or percentage"
     */
    @Test
    public void test_Create_Discount_Entity_Using_Both_Percentage_And_Amount(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(utils.generateUUID());
        discount.setName(utils.generateString());
        discount.setDescription("");
        discount.setImageLookupKeys(Collections.<String>emptyList());
        discount.setAmount(333, "USD");
        discount.setPercentage("20");
        discount.setExternalReference(utils.generateString());

        given()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .contentType(ContentType.JSON)
                .body(discount)
                .when()
                .post("/discounts")
                .then().statusCode(422);
    }

}

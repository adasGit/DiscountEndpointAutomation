package discount.test;

import discount.data.Discount;
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


//ToDo: Convert JSON to Java object
//ToDo: Insert test data
/**
 * The test suite
 */
public class Product_Discount_Test_Suite {
    private static Logger logger = LoggerFactory.getLogger(Product_Discount_Test_Suite.class);
    private Utils utils;
    private Token token;

    @BeforeClass
    public void setUpClass() {
        BasicConfigurator.configure();
        utils = new Utils();
        token = new Token();
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
                .contentType(ContentType.JSON)
                .extract().path("percentage");

        Assert.assertEquals(value, "100", "Failed to retrieve a particular discount!");
    }

    @Test
    public void test_Invalid_Token_Error_Message(Method method) {
        logger.info("Test Name: " + method.getName());
        Response res = given()
                .header("Authorization", "Bearer eyJraWQiOiIxNDkwNTM4MzE5MzExIiwidH")
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
                .header("Authorization", "Bearer eyJraWQiOiIxNDkwNDUxOTE4Njc0IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwNTQ0NjU4LCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.PvIBGWPybhTvje24cLjZO4WLdBI7RbbUrcVZuA9ctC_Kko-LiCPWY5PYWK2uumOOAMfzqFHcG7dPiToKdYGdA4Mne5LeTOW97ioyrUdvUWzLOHPPpNUMsePFxBqht1PjzhPxMPlOzFIzxNE_hGOqksiqIbwN3BkQvYZf2NM3c2X8UcNFGAJEKS8hizsgdPcuzNtX65RdSMjhrV2xD0BOtsLWbDnx17kKYkCQEw453SpSKMfkmNcbif_aftTmQwlvS1WXToC2iG3qbee3ctllIpu7CDFQER0sER_cfqhg0Z56CJb34nzBHWCabCnPjq0_QfYeMY4iY_2DNA-zds08TA")
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        Assert.assertTrue(res.header("WWW-Authenticate").contains("ACCESS_TOKEN_EXPIRED"));
    }

    @Test
    public void test_Wrong_Credential_Response_Code(Method method) {
        logger.info("Test Name: " + method.getName());
        given()
                .header("Authorization", "Bearer eyJraWQiOiIxNDkwNTM4MzE5MzExIiwidH")
                .when()
                .get("/discounts")
                .then().statusCode(401);
    }

    @Test
    public void test_Create_Single_Discount_Entity(Method method) {
        logger.info("Test Name: " + method.getName());
        Discount discount = new Discount();
        discount.setUuid(utils.generateUUID());
        discount.setAmount(3,"");

    }

    @Test
    public void test_Create_Duplicate_Discount_Entity(Method method) {
        logger.info("Test Name: " + method.getName());

    }

    @Test
    public void test_Duplicate_Discount_Entity_Exist(Method method) {
        logger.info("Test Name: " + method.getName());

    }

}

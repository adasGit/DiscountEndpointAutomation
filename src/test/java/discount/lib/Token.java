package discount.lib;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


public class Token {
    private String accessToken;
    private String refreshToken;

    public Token() {
        this.accessToken = "eyJraWQiOiIxNDkwNTM4MzE5MzExIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwNjMzMzkwLCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.S635vqM55hq_pU0R99e5FuC0bHIfQIPBkdWf1msXTnCiFSCg_QCjRAk_hyb777coJfasT0xSaCOoCchhwYcjGaj60EL7A1OpfjeM9GwUgGweqwLukMF0mSZeywVD762w7rRl0kMd2RBEvLvA6HTaXLyv9CpJ_1z-buFPS-BEuA6W2ASXZGFmpQaIOgEDCM7UUC2k9Nh0bsaYCfo388ZiS8GUmW29ncZ56BYabxoIExIA7zXKIH0kUagxtmns1sF3taPadZJfEm0XWYOojoih4jvPXQBoeDa_hTu84GuNl0vEToKsBbjRumjmRb8LGrybJYtTLaAcv0cXNqpef7Y7yA";
        this.refreshToken = "IZSECd6056e5c-2db1-4c3d-b912-5d30506d89fd";
    }

    public String getAccessToken() { return this.accessToken; }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public void refreshAccessToken() {
        RestAssured.reset();
        RestAssured.baseURI = "https://oauth.izettletest.com";

        String res = given()
                .contentType("application/x-www-form-urlencoded")
                .body("grant_type=refresh_token&client_id=0bb91e02-7e53-4742-9bcc-7388d694e1fe&refresh_token="+getRefreshToken())
                .when()
                .post("/token")
                .then()
                .contentType(ContentType.JSON)
                .extract().path("access_token");

        System.out.println(res);
        this.setAccessToken(res);
    }

    public boolean isExpired() {
        RestAssured.reset();
        RestAssured.baseURI = "https://products.izettletest.com";
        RestAssured.basePath = "/organizations/1f85f1c0-ff2a-11e6-9fea-e83146fb0828";

        Response res = given()
                .header("Authorization", "Bearer "+getAccessToken())
                .when()
                .get("/discounts")
                .then()
                .extract().response();

        System.out.println(res.headers());
        System.out.println(res.header("WWW-Authenticate"));
        try {
            return (res.header("WWW-Authenticate").contains("EXPIRED")) ? true : false;
        }catch (NullPointerException e) {
            return false;
        }
    }

    public void verifyToken() {
        if(this.isExpired())
            this.refreshAccessToken();
    }
}

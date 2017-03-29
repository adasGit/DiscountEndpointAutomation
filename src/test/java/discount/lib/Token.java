package discount.lib;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;


/**
 * This class is to manage access token
 */
public class Token {
    private String accessToken;
    private String refreshToken;

    public Token() {
        this.accessToken = "eyJraWQiOiIxNDkwNjc5MDQyNzE5IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwNzM3ODc4LCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.f1TzyxJ8bqHevSEMTT5rNdEMnVk_h9sONzBsf2zc_z2Y-hvWjNFdP5MoriYbOJefLW2JCTHcU7DR0g6HUzTKyXwM9hXxRUDZyIjL8zd_1N86n74SRqlnXxIa7KdkENvAlNCOCno34iCVwZY9PTS5tIytsvPMx2na5oJVN852tknnTxCPwta0TMmspHjiWZkosF4Bz9aenTdQIm-13u55XyyIJ9gtQZ6k8UCIsOrJoKRhGQfqhE1ChBVf3-52iK7vz6g_NKhj2veJ6FQgSa9ve4MqsBII7FTiTJwvVphoHvojc9W14zVhO2mvHLd9NQPXg4Gpok6vvmc35u-TalfXOQ";
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

        //System.out.println(res.headers());
        //System.out.println(res.header("WWW-Authenticate"));
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

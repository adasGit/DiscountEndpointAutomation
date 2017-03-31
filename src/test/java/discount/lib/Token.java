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
        this.accessToken = "eyJraWQiOiIxNDkwODUxODQzNjI1IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwOTE5NDQzLCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.cCWNa0SU6kGJ4rfL7L9qD5GNclti2Yz8XeKOZa12ZYnQ58tKAyd6eiq1E49DItnKVHr-YILaBYmdf_DEapvhoa-QCeIaR8eGjBBEdOo_MwhG0CQsY7uIhr7yWSKPuuIeZYrC4PgSBQsdJotFCkumQ_LUkoVDuEBm2qC3OM7Wg2h8Pb3wC5oNv8ky78PmscJhWj5BBLmZ1up32CYI5OYQvTQJROwWQoaEqtvEqDaHcsOtJw2oTg9ehG2XOEWrHCcEjgZ3v-69SdHji3RcEEKSOZILlNxZV6mEwzviNWGmnA4znzOWMYV_dGkJaiGQay1UYYGlZ5ffAZZPaZIzicDK9g";
        this.refreshToken = "IZSECd6056e5c-2db1-4c3d-b912-5d30506d89fd";
    }

    public String getAccessToken() { return this.accessToken; }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public String getInvalidAccessToken() { return "eyJraWQiOiIxNDkwNzExMTIwMTQ1IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwODEwMDU5LCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.fqp9iSNqnG39CZxsyFFqFe9jtT12zM0Ry7j6hQeD19943ZTCT6YD2MeH1tb7sKEHaD00VWgdkSymrawAIeTKitUt5qRSjtKolrJIMB6BG3p2pwPCSCmW84FEQbsT-2gs0Izp9sc2b6oPKerq0jWUZrrJpBNTzyvF-50mETYzzpQLuNuEvvYyKHHZsVg8Z4Orn-6FYPjcNWZxpOp49-dRUbsPxSjoJ8paBOmKdwfKVUtZhobdNzU95pdIpYGqB_-CwFSm8hONLDbj4vZTWqa84t4IIppiSHOQq-X2YDX-ctfuz9Xqyl-jWroztmdFjFXBQrwHQlTp0LWQObHjOSyvTT"; }

    public String getExpiredAccessToken() { return "eyJraWQiOiIxNDkwNDUxOTE4Njc0IiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJpWmV0dGxlIiwiYXVkIjoiQVBJIiwiZXhwIjoxNDkwNTQ0NjU4LCJzdWIiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJzY29wZSI6WyJBTEw6SU5URVJOQUwiLCJSRUFEOkZJTkFOQ0UiLCJSRUFEOlBST0RVQ1QiLCJSRUFEOlBVUkNIQVNFIiwiUkVBRDpVU0VSSU5GTyIsIldSSVRFOkZJTkFOQ0UiLCJXUklURTpQUk9EVUNUIiwiV1JJVEU6UFVSQ0hBU0UiLCJXUklURTpVU0VSSU5GTyJdLCJ1c2VyIjp7InVzZXJUeXBlIjoiVVNFUiIsInV1aWQiOiIxZjlhNjQyMC1mZjJhLTExZTYtOWM5OS1mOWQzNDNlMDNiOTQiLCJvcmdVdWlkIjoiMWY4NWYxYzAtZmYyYS0xMWU2LTlmZWEtZTgzMTQ2ZmIwODI4IiwidXNlclJvbGUiOiJPV05FUiJ9LCJjbGllbnRfaWQiOiIwYmI5MWUwMi03ZTUzLTQ3NDItOWJjYy03Mzg4ZDY5NGUxZmUifQ.PvIBGWPybhTvje24cLjZO4WLdBI7RbbUrcVZuA9ctC_Kko-LiCPWY5PYWK2uumOOAMfzqFHcG7dPiToKdYGdA4Mne5LeTOW97ioyrUdvUWzLOHPPpNUMsePFxBqht1PjzhPxMPlOzFIzxNE_hGOqksiqIbwN3BkQvYZf2NM3c2X8UcNFGAJEKS8hizsgdPcuzNtX65RdSMjhrV2xD0BOtsLWbDnx17kKYkCQEw453SpSKMfkmNcbif_aftTmQwlvS1WXToC2iG3qbee3ctllIpu7CDFQER0sER_cfqhg0Z56CJb34nzBHWCabCnPjq0_QfYeMY4iY_2DNA-zds08TA"; }

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

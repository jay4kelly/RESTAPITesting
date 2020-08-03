package my.example.tests.stepDefinitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import my.example.model.User;
import my.example.tests.support.APIResources;
import my.example.tests.support.TestMain;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.util.List;

import static io.restassured.RestAssured.given;


public class StepDefinitions extends TestMain {

    private final static Logger log = LogManager.getLogger(StepDefinitions.class.getName());
    private RequestSpecification res;
    private Response response;

    //Common properties across resources for use by multiple scenarios.
    private static String lastId;  // last specific id from result of get or post

    //User properties - for use by multiple scenarios
    my.example.model.User user;


    @Given("Add new User Payload with {string} {string} {string} {string}")
    public void add_new_User_Payload_with(String firstName, String lastName, String gender, String email) {
        log.info("Start add new User payload");
        user = new User().withFirstName(firstName).withLastName(lastName).withGender(gender)
                .withEmail(email);
        res = given().spec(requestSpecification()).body(user);
    }

    @When("User calls {string} with {string} http request")
    public void user_calls_with_http_request(String resource, String method) {
        invokeRequest(resource,method,null);
    }

    @Then("the api call returns with a status code {int}")
    public void the_api_call_returns_with_a_status_code(int code) {
        log.info("user checks response status");
        JsonPath json = stringToJson(response.asString());
        Assert.assertEquals(getCode(json), code, "status code not correct");
        log.info("Generated user with id:" + lastId);
    }


    @Then("User calls {string} with {string} http request for added user")
    public void user_calls_with_http_request_for_added_user(String resource, String method) {
        log.info(method + "will be called on " + resource + "with id: " + lastId);
        invokeRequest(resource,method, lastId);
        log.info(method + "was called on " + resource + "with id: " + lastId);
    }

    @And("verify user created maps to returned User")
    public void verifyUserCreatedMapsToReturnedUser() {
        log.info("Verify user returned is the same as the user added");
        JsonPath json = stringToJson(response.asString());
        String actual_id = json.get("result.id");
        Assert.assertEquals(actual_id, lastId, "user id is not correct");
        Assert.assertEquals(json.get("result.first_name"),user.getFirstName(),"first_name does not match");
        Assert.assertEquals(json.get("result.last_name"),user.getLastName(),"last_name does not match");
        Assert.assertEquals(json.get("result.gender"),user.getGender(),"gender does not match");
        Assert.assertEquals(json.get("result.email"),user.getEmail(),"email does not match");
        log.info("User returned is the same as the user added");
    }

    private void invokeRequest(String resource, String method, String id) {
        String pathId = (id != null) ? "/" + id : "";
        APIResources resourceAPI = APIResources.valueOf(resource);
        if (method.equalsIgnoreCase("POST")) {
            response = res.when().post(resourceAPI.getResource() + pathId);
            JsonPath json = stringToJson(response.asString());
            lastId = json.get("result.id");
        } else if (method.equalsIgnoreCase("GET")){
            response = res.when().get(resourceAPI.getResource() + pathId);
        } else if (method.equalsIgnoreCase("DELETE")){
            response = res.when().delete(resourceAPI.getResource() + pathId);
        }
    }

    @Given("Add Delete {string} Payload")
    public void addDeletePayload(String resource) {
        log.info("Add delete " + resource + " payload");
        res = given().spec(requestSpecification());
        log.info("Delete payload created");
    }

    @Given("Add Get Users Payload")
    public void addGetUsersPayload() {
        log.info("Define  for get users");
        res = given().spec(requestSpecification());
        log.info("Completed request spec for get users");
    }

    @And("result contains users")
    public void resultContainsUsers() {
        JsonPath json = stringToJson(response.asString());
        List<String> list = json.getList("result");
        Assert.assertFalse(list.isEmpty(),"List of users is empty");
    }
}

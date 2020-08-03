package my.example.tests.support;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;


public class TestMain {

    private final static Logger log = LogManager.getLogger(TestMain.class.getName());
    private static PrintStream restLog = null;
    public static AppProperties properties = null;
    private static RequestSpecification req;

    private static final Map<String, String> HEADERS = new HashMap<>();

    static {
        if (properties == null){
            properties = new AppProperties();
            RestAssured.baseURI = properties.getBaseURL();
            log.info("baseURI: " + RestAssured.baseURI );
        }
        if (restLog == null){
            try {
                restLog = new PrintStream(new FileOutputStream("target/logging.txt"));
            } catch (FileNotFoundException e) {
                log.fatal("unable to create PrintStream rest_log",e);
            }
        }

        if (HEADERS.isEmpty()){
            HEADERS.put("Authorization","Bearer " + properties.getBearerToken());
            HEADERS.put("Content-Type","application/json");
            HEADERS.put("Accept","*/*");
        }
    }


    public RequestSpecification requestSpecification() {
        if (req == null) {
            req = new RequestSpecBuilder().setBaseUri(RestAssured.baseURI)
                    .addHeaders(HEADERS)
                    .addFilter(RequestLoggingFilter.logRequestTo(restLog))
                    .addFilter(ResponseLoggingFilter.logResponseTo(restLog)).setContentType(ContentType.JSON).build();
        }

        return req;
    }

    public int getCode (JsonPath json){
        return json.get("_meta.code");
    }

    public static JsonPath stringToJson(String value) {
        return new JsonPath(value);
    }

}

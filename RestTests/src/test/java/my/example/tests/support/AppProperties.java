package my.example.tests.support;

import io.restassured.RestAssured;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private final static String PROPERTY_FILE = "app.properties";

    // Property name constants
    public static final String BASE_URL = "BASE_URL";
    public static final String BEARER_TOKEN = "BEARER_TOKEN";

    private final static Logger log = LogManager.getLogger(AppProperties.class.getName());
    static Properties prop;


    public AppProperties() {
        InputStream is = getClass().getClassLoader().getResourceAsStream(PROPERTY_FILE);
        prop = new Properties();
        try {
            prop.load(is);
            log.info("loaded " + PROPERTY_FILE );
        } catch (IOException e) {
            log.log(Level.FATAL,"unable to load app.properties",e);
        }
    }

    public String getBaseURL(){
        return prop.getProperty(BASE_URL);
    }
    public String getBearerToken(){
        return prop.getProperty(BEARER_TOKEN);
    }

}

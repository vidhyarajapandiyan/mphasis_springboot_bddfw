package com.mphasis.qe.utils;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.util.Map;
import java.util.logging.Logger;
/****************************************************************************************
 * @author manoj chavan
 ****************************************************************************************/
@Slf4j
public class TearDown {

    private WebDriver driver;

    public TearDown() {
        this.driver = Setup.webdriver;
    }

    @After("@web")
    public void quitDriver(Scenario scenario){

        if(scenario.isFailed()){
           saveScreenshotsForScenario(scenario);
        }
        log.info("Closing the app");
        this.driver.manage().deleteAllCookies();
        this.driver.quit();
    }

    @After("@api")
    public void quitAPITest(Scenario scenario){
        if(scenario.isFailed()) {
            Map.Entry<String, Response> map = ApiUtil.requestResponseMap.entrySet().iterator().next();
            scenario.write("Request: " + map.getKey() + "\n" +
                    "Response:" + map.getValue().asString() + "\n" +
                    "Status Code: " + map.getValue().getStatusCode());
        }
    }

    private void saveScreenshotsForScenario(final Scenario scenario) {

        final byte[] screenshot = ((TakesScreenshot) driver)
                .getScreenshotAs(OutputType.BYTES);
        scenario.embed(screenshot, "image/png", "ErrorScreenshot" + scenario.getName());
    }
}

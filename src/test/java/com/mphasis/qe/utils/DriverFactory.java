package com.mphasis.qe.utils;

import java.util.logging.Level;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.mphasis.qe.PropertySourceResolver;
/****************************************************************************************
 * @author Pankaj Sao : DriverFactory class to return remoteDriver instance of browser
 ****************************************************************************************/

public class DriverFactory extends RemoteWebDriver {
	@Autowired
    private PropertySourceResolver propertySourceResolver;
	
	public RemoteWebDriver driver;
	public synchronized RemoteWebDriver createInstance(String browser)throws Exception {
		if (browser.equalsIgnoreCase("firefox")){        		
			System.out.println(" Executing on FireFox");
			System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "\\library\\geckodriver.exe");
			//System.setProperty("webdriver.gecko.driver",propertySourceResolver.getGeckoDriverPath());
			DesiredCapabilities cap = DesiredCapabilities.firefox();
			cap.setCapability("marionette", true);
			cap.setBrowserName("firefox");
			cap.setJavascriptEnabled(false);
			driver = new FirefoxDriver(cap);
		} 

		else if (browser.equalsIgnoreCase("chrome")){
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\library\\chrome\\91\\chromedriver.exe");
			//System.setProperty("webdriver.chrome.driver",propertySourceResolver.getChromeDriverPath());
			System.out.println(" Executing on Chrome");
			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability("marionette", true);
			cap.setBrowserName("chrome");
			LoggingPreferences logPrefs = new LoggingPreferences();
			logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
			cap.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
			cap.setPlatform(Platform.ANY);
			driver = new ChromeDriver();							
		} 

		else if (browser.equalsIgnoreCase("ie")){        		
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+ "\\library\\ie.exe");
			//System.setProperty("webdriver.chrome.driver",propertySourceResolver.getIeDriverPath());
			System.out.println(" Executing on Internet Explorer");
			DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,true);
			cap.setCapability("marionette", true);
			cap.setBrowserName("internet explorer");
			cap.setPlatform(Platform.ANY);
			cap.setCapability("nativeEvents", false);
			cap.setCapability("unexpectedAlertBehaviour", "accept");
			cap.setCapability("ignoreProtectedModeSettings", true);
			cap.setCapability("disable-popup-blocking", true);
			cap.setCapability("enablePersistentHover", true);
			cap.setCapability("ignoreZoomSetting", true);
			driver = new InternetExplorerDriver(cap);
		} 
		else if (browser.equalsIgnoreCase("edge")){        		
			System.setProperty("webdriver.edge.driver", System.getProperty("user.dir")+ "\\library\\edge.exe");
			//System.setProperty("webdriver.chrome.driver",propertySourceResolver.getEdgeDriverPath());
			System.out.println(" Executing on Edge browser");
			DesiredCapabilities cap = DesiredCapabilities.edge();
			driver = new EdgeDriver(cap);
		}
		driver.manage().window().maximize();
		return driver;
	}

}
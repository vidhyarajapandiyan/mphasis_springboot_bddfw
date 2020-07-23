package com.mphasis.qe.utils;

import com.mphasis.qe.PropertySourceResolver;
import com.ulisesbocchio.jasyptspringboot.EncryptablePropertyResolver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author : manoj chavan Parent class for all test classes contains basic
 *         methods and test case configuration
 */
@Slf4j
public class Setup {

	private static String APPIUM_WEB_DRIVER_SERVER_URL = null;

	AndroidDriver androidDriver;
	IOSDriver iosDriver;

	@Autowired
	private PropertySourceResolver propertySourceResolver;

	private static String platformName;
	private static String browserName;
	public static String username;
	public static String password;
	public static WebDriver webdriver;
	public static String baseUrl;
	public static String dataSource;
	public static String dataPath;

	// webdriverManagerFlag
	public boolean webdriverManagerFlag = false;;

	@Before("@web")
	public void setUp() throws Exception {
		platformName = propertySourceResolver.getPlatformName();
		browserName = propertySourceResolver.getBrowserName();
		username = JasyptEncryptor.decrypt(propertySourceResolver.getUserId());
		password = JasyptEncryptor.decrypt(propertySourceResolver.getPassword());
		dataSource = propertySourceResolver.getDataSource();
		dataPath = propertySourceResolver.getDataPath();

		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "apps/test.apk");

		if (browserName.equalsIgnoreCase("chrome")) {

			if (webdriverManagerFlag = false) {
				System.setProperty("webdriver.chrome.driver", propertySourceResolver.getChromeDriverPath());
			}

		} else if (browserName.equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.driver", propertySourceResolver.getGeckoDriverPath());
		} else if (browserName.equalsIgnoreCase("ie")) {
			System.setProperty("webdriver.ie.driver", propertySourceResolver.getIeDriverPath());
		}

		DesiredCapabilities capabilities = new DesiredCapabilities("", "", Platform.ANY);
		this.APPIUM_WEB_DRIVER_SERVER_URL = propertySourceResolver.getPerfectoUrl();
		this.baseUrl = propertySourceResolver.getAppBaseUrl();

		if (platformName.equalsIgnoreCase("android")) {
			File app = new File(appDir, propertySourceResolver.getAndroidApkFile());
			capabilities.setCapability("platformName", platformName);
			capabilities.setCapability("deviceName", propertySourceResolver.getAndroidDeviceName());
			capabilities.setCapability("platformVersion", propertySourceResolver.getAndroidPlatformVersion());
			capabilities.setCapability("browserName", browserName);
			capabilities.setCapability("securityToken", propertySourceResolver.getPerfectoToken());
//            capabilities.setCapability("app", app.getAbsolutePath());
			androidDriver = new AndroidDriver(new URL(APPIUM_WEB_DRIVER_SERVER_URL), capabilities);
			androidDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			webdriver = androidDriver;
		} else if (platformName.equalsIgnoreCase("ios")) {
			File app = new File(appDir, "test.app");
			// Configure the desired capabilities
			capabilities.setCapability("deviceName", propertySourceResolver.getIosDeviceName());
			capabilities.setCapability("platformVersion", propertySourceResolver.getIosPlatformVersion());
			capabilities.setCapability("platformName", platformName);
			capabilities.setCapability("browserName", browserName);
			capabilities.setCapability("securityToken", propertySourceResolver.getPerfectoToken());
//            capabilities.setCapability("app", app.getAbsolutePath());
			capabilities.setCapability("launchTimeout", 9000000);

			iosDriver = new IOSDriver(new URL(APPIUM_WEB_DRIVER_SERVER_URL), capabilities);
			iosDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			webdriver = iosDriver;
		} else {
			if (browserName.equalsIgnoreCase("chrome")) {

				if (webdriverManagerFlag = false) {
					ChromeOptions options = new ChromeOptions();
					options.addArguments("start-maximized"); // open Browser in maximized mode
					options.addArguments("disable-infobars"); // disabling infobars
					options.addArguments("--disable-extensions"); // disabling extensions
					options.addArguments("--disable-gpu"); // applicable to windows os only
					options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
					options.addArguments("--no-sandbox"); // Bypass OS security model
					webdriver = new ChromeDriver(options);
				} else {
					webdriver = chromeDriverSetup();

				}

			} else if (browserName.equalsIgnoreCase("firefox")) {
				FirefoxOptions options = new FirefoxOptions();
				options.addArguments("start-maximized"); // open Browser in maximized mode
				options.addArguments("--disable-extensions"); // disabling extensions
				options.addArguments("--disable-gpu"); // applicable to windows os only
				options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
				options.addArguments("--no-sandbox"); // Bypass OS security model

				webdriver = new FirefoxDriver(options);
			} else if (browserName.equalsIgnoreCase("ie")) {
				InternetExplorerOptions options = new InternetExplorerOptions();
				// options.destructivelyEnsureCleanSession();
				options.ignoreZoomSettings();

				webdriver = new InternetExplorerDriver(options);
			}

			webdriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			webdriver.manage().deleteAllCookies();
			webdriver.manage().window().maximize();
		}
	}

	// WebDriverManager implementation

	public WebDriver chromeDriverSetup() {

		Generic generic = new Generic();
		String proxyDetailsWFG = "";
		String splitChromeVersion = generic.getChromeVersion();
		String driverVersion = generic.getDriverVersion(splitChromeVersion);
		WebDriverManager.chromedriver().version(driverVersion).proxy(proxyDetailsWFG).setup();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model
		return new ChromeDriver(options);

	}
}

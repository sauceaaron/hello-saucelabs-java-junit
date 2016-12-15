import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class SettingChromeOptions
{
    String LOCAL_URL = "http://localhost:4444/wd/hub";
    String SAUCE_URL = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";

    String SAUCE_USERNAME;
    String SAUCE_ACCESS_KEY;

    String SELENIUM_PLATFORM;
    String SELENIUM_BROWSER;
    String SELENIUM_VERSION;

    DesiredCapabilities desiredCapabilities;
    Capabilities capabilities;
    URL remoteUrl;
    RemoteWebDriver driver;

    Boolean USE_SAUCE_LABS = true;

    @Before
    public void setup() throws Exception
    {
        loadSettings();
    }

    @After
    public void teardown() throws Exception
    {
        if (driver != null)
        {
            Thread.sleep(5 * 1000);
            driver.quit();
        }

    }

    @Ignore
    @Test
    public void disable_javascript_on_chrome() throws Exception
    {
        desiredCapabilities = getDesiredCapabilities();
        remoteUrl = getRemoteUrl();

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.setExperimentalOption("prefs", "{'profile.managed_default_content_settings.images': 2}");
//        chromeOptions.addArguments(Arrays.asList("--disable-javascript"));
//
//        desiredCapabilities.setCapability("chrome.switches", Arrays.asList("--disable-javascript"));
//
//        Map<String, Object> preferences = new HashMap<>();
//        preferences.put( "browser.startup.homepage", "http://one-shore.com" );
//        preferences.put("homepage_is_newtabpage", true);

//        desiredCapabilities.setCapability( ChromeOptions.CAPABILITY, preferences );

        driver = new RemoteWebDriver(remoteUrl, desiredCapabilities);

        driver.get("https://www.whatismybrowser.comxxx/detect/is-javascript-enabled");

        String isJavascriptEnabled = driver.findElement(By.cssSelector(".detected_result")).getText();
        System.out.println("is javascript enabled: " + isJavascriptEnabled);

        assertThat(isJavascriptEnabled, is("Yes"));

    }


    public void loadSettings() throws Exception
    {
        SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        SELENIUM_PLATFORM = System.getenv("SELENIUM_PLATFORM");
        SELENIUM_BROWSER = System.getenv("SELENIUM_BROWSER");
        SELENIUM_VERSION = System.getenv("SELENIUM_VERSION");

        System.out.println("SAUCE_USERNAME: " + SAUCE_USERNAME);
        System.out.println("SAUCE_ACCESS_KEY: " + SAUCE_ACCESS_KEY);
        System.out.println("SAUCE_URL: " + SAUCE_URL);

        System.out.println("SELENIUM_PLATFORM:" + SELENIUM_PLATFORM);
        System.out.println("SELENIUM_BROWSER:" + SELENIUM_BROWSER);
        System.out.println("SELENIUM_VERSION:" + SELENIUM_VERSION);

        System.out.println("USE_SAUCE_LABS:" + USE_SAUCE_LABS);


        if (SAUCE_USERNAME != null && SAUCE_ACCESS_KEY != null)
        {
            SAUCE_URL = SAUCE_URL.replace("SAUCE_USERNAME", SAUCE_USERNAME).replace("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY);
        }
    }

    public DesiredCapabilities getDesiredCapabilities()
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        Platform platform = getPlatform(SELENIUM_PLATFORM);
        desiredCapabilities.setCapability(CapabilityType.PLATFORM, platform);
        desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        desiredCapabilities.setVersion(SELENIUM_VERSION);

        System.out.println("desiredCapabilities:" + desiredCapabilities);

        return desiredCapabilities;
    }

    public Platform getPlatform(String platformName)
    {
        return Platform.ANY;
    }

    public URL getRemoteUrl() throws MalformedURLException
    {
        URL remoteUrl;

        if (USE_SAUCE_LABS == true)
        {
            remoteUrl = new URL(SAUCE_URL);
        }
        else
        {
            remoteUrl = new URL(LOCAL_URL);
        }

        System.out.println("remoteUrl: " + remoteUrl);

        return remoteUrl;
    }

}

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;

public class HelloSauceLabs
{
    RemoteWebDriver driver;

    @Before
    public void before() throws Exception {
        System.out.println("setting up...");

        URL sauceConnectUrl = getSauceConnectUrl();
        DesiredCapabilities desiredCapabilities = setDesiredCapabilities();
        driver = new RemoteWebDriver(sauceConnectUrl, desiredCapabilities);

        System.out.println("connected to remote webdriver at: " + sauceConnectUrl);

        Capabilities actualCapabilities = driver.getCapabilities();
        compareCapabilities(desiredCapabilities, actualCapabilities);
    }

    @Test
    public void open_home_page() throws Exception
    {
        System.out.println("running test...");

        driver.get(SauceLabs.HomePage.url);
        assertEquals(SauceLabs.HomePage.Expected.title, driver.getTitle());
    }

    @After
    public void after()  throws Exception
    {
        System.out.println("SAUCE_ONDEMAND_BROWSERS: " + System.getenv("SAUCE_ONDEMAND_BROWSERS"));

        if (driver != null) {
            System.out.println("killing webdriver");
            driver.quit();
        }
        else {
            System.out.println("webdriver is already dead!");
        }
    }

    public URL getSauceConnectUrl() throws MalformedURLException
    {
        String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        System.out.println("SAUCE_USERNAME: " + SAUCE_USERNAME);
        System.out.println("SAUCE_ACCESS_KEY: " + SAUCE_ACCESS_KEY);

        String sauceLabsUrlString = String.format("http://%s:%s@ondemand.saucelabs.com:80/wd/hub", SAUCE_USERNAME, SAUCE_ACCESS_KEY);
        URL sauceLabsUrl = new URL(sauceLabsUrlString);
        System.out.println("sauceLabsURL: " + sauceLabsUrl);
        return sauceLabsUrl;
    }

    public DesiredCapabilities setDesiredCapabilities()
    {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability("platform", "OS X 10.11");
        capabilities.setCapability("version", "54");

        return capabilities;
    }

    public void compareCapabilities(Capabilities desired, Capabilities actual)
    {
        System.out.println("desired capabilities: ");
        reportCapabilities(desired);

        System.out.println("actual capabilities: ");
        reportCapabilities(actual);

        System.out.println("family: " + actual.getPlatform().family());
        System.out.println("major version: " + actual.getPlatform().getMajorVersion());
        System.out.println("minor version: " + actual.getPlatform().getMinorVersion());

        for (String part : actual.getPlatform().getPartOfOsName()) {
            System.out.println("part: " + part);
        }

        System.out.println(actual.getPlatform().toString());

        assertTrue(desired.getPlatform().is(actual.getPlatform())); // returns MAC
        assertEquals(desired.getBrowserName(), actual.getBrowserName());
        assertEquals(desired.getVersion(), actual.getVersion().substring(0,2)); // returns 54.0.2840.59
    }


    public void reportCapabilities(Capabilities capabilities)
    {
        System.out.println("platform: " + capabilities.getPlatform());
        System.out.println("browser: " + capabilities.getBrowserName());
        System.out.println("version: " + capabilities.getVersion());
    }
}

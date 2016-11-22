import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

public abstract class SauceTestBase
{
    protected static final String SauceUrlTemplate = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";

    @Rule
    public TestName name= new TestName();

    protected URL SauceUrl;
    protected RemoteWebDriver driver;
    Logger log;

    public SauceTestBase()
    {
        log = Logger.getLogger(this.getClass().getSimpleName());
        log.info("Starting logger");
    }

    @Before
    public void acquireWebDriverInstance() throws MalformedURLException
    {
        log.info("acquiring WebDRiver instance");
        driver = new RemoteWebDriver(getSauceUrl(), getDesiredCapabilities());
        log.info(driver.getCapabilities().getPlatform().name());
        log.info(String.valueOf(driver.getCapabilities().getPlatform().toString());
        log.info(String.valueOf(driver.getCapabilities().getPlatform().toString()));
        log.info(driver.getCapabilities().getPlatform().getPartOfOsName().toString());
        log.info(driver.getCapabilities().getBrowserName());
        log.info(driver.getCapabilities().getVersion());
    }

    @After
    public void disposeWebDriverInstance()
    {
        if (driver != null)
        {
            driver.quit();
            System.out.println("webdriver quit");
        }
        else
        {
            System.out.println("webdriver doesn't exist");
        }
    }

    public URL getSauceUrl() throws MalformedURLException
    {
        // Get SAUCE_USERNAME and SAUCE_ACCESS_KEY from environment variables
        String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        System.out.println("SAUCE_USERNAME: " + SAUCE_USERNAME);
        System.out.println("SAUCE_ACCESS_KEY: " + SAUCE_ACCESS_KEY);

        String SauceUrlString = SauceUrlTemplate
                .replace("SAUCE_USERNAME", SAUCE_USERNAME)
                .replace("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY);
        System.out.println("SauceUrlString: " + SauceUrlString);

        URL SauceUrl = new URL(SauceUrlString);
        System.out.println("SauceUrl: " + SauceUrl);

        return SauceUrl;
    }

    public DesiredCapabilities getDesiredCapabilities()
    {
        // Get Capabilities from environment variables (set by Jenkins OnDemand Plugin)
        String SAUCE_ONDEMAND_BROWSERS = System.getenv("SAUCE_ONDEMAND_BROWSERS");
        String SELENIUM_BROWSER = System.getenv("SELENIUM_BROWSER");
        String SELENIUM_VERSION = System.getenv("SELENIUM_VERSION");
        String SELENIUM_PLATFORM = System.getenv("SELENIUM_PLATFORM");
        String SELENIUM_DRIVER = System.getenv("SELENIUM_DRIVER");

        System.out.println("SAUCE_ONDEMAND_BROWSERS: " + SAUCE_ONDEMAND_BROWSERS);
        System.out.println("SELENIUM_DRIVER: " + SELENIUM_DRIVER);
        System.out.println("SELENIUM_PLATFORM: " + SELENIUM_PLATFORM);
        System.out.println("SELENIUM_BROWSER: " + SELENIUM_BROWSER);
        System.out.println("SELENIUM_VERSION: " + SELENIUM_VERSION);

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        desiredCapabilities.setVersion(SELENIUM_VERSION);
        desiredCapabilities.setCapability(CapabilityType.PLATFORM, SELENIUM_PLATFORM);

        return desiredCapabilities;
    }
}

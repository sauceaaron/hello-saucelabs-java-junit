import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class SauceTestBase
{
    protected static final String SauceUrlTemplate = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";

    @Rule
    public TestName name= new TestName();

    protected RemoteWebDriver driver;

    @Before
    public void acquireWebDriverInstance() throws MalformedURLException
    {
        System.out.println("acquiring WebDriver instance");

        DesiredCapabilities desiredCapabilities = getDesiredCapabilities();

        String JOB_NAME = System.getenv("JOB_NAME");
        String BUILD_NUMBER = System.getenv("BUILD_NUMBER");

        System.out.println("JOB_NAME: " + JOB_NAME);
        System.out.println("BUILD_NUMBER: " + BUILD_NUMBER);

        desiredCapabilities.setCapability("build",  JOB_NAME + "__" + BUILD_NUMBER);

        driver = new RemoteWebDriver(getSauceUrl(), desiredCapabilities);

        System.out.println("platform name: " + driver.getCapabilities().getPlatform().name());
        System.out.println("platform majorVersion: " + driver.getCapabilities().getPlatform().getMajorVersion());
        System.out.println("platform minorVersion: " + driver.getCapabilities().getPlatform().getMinorVersion());

        System.out.println("browserName: " + driver.getCapabilities().getBrowserName());
        System.out.println("version: " + driver.getCapabilities().getVersion());

        for (String part : driver.getCapabilities().getPlatform().getPartOfOsName())
        {
            System.out.println("part: " + part);
        }



        System.out.println("=====");
        System.out.println("name: " + name.getMethodName());
        System.out.println("=====");

    }

    @After
    public void disposeWebDriverInstance()
    {
        if (driver != null)
        {
            driver.quit();
            System.out.println("webdriver quit");

            System.out.println("=====");
            System.out.println("build: " + "A FAKE BUILD NUMBER");
            System.out.println("=====");
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

//        String TUNNEL_IDENTIFIER = System.getenv("TUNNEL_IDENTIFIER");
//        desiredCapabilities.setCapability("tunnelIdentifier", TUNNEL_IDENTIFIER);
        return desiredCapabilities;
    }
}

import com.saucelabs.saucerest.SauceREST;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public abstract class SauceTestBase
{
    @Rule
    public TestName testName = new TestName();

    protected Properties properties;
    protected DesiredCapabilities desiredCapabilities;
    protected RemoteWebDriver driver;
    protected SauceREST sauce;

    protected String SAUCE_URL = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";

    protected String SAUCE_USERNAME;
    protected String SAUCE_ACCESS_KEY;
    protected String SELENIUM_PLATFORM;
    protected String SELENIUM_BROWSER;
    protected String SELENIUM_VERSION;
    protected String SELENIUM_DRIVER;
    protected String SAUCE_ONDEMAND_BROWSERS;

    protected String JOB_NAME;
    protected String BUILD_NUMBER;
    protected String BUILD_NAME;
    protected String JENKINS_BUILD_NUMBER;

    protected String TEST_NAME;

    @Before
    public void acquireWebDriverInstance() throws MalformedURLException
    {
        loadTestProperties();
        desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        desiredCapabilities.setVersion(SELENIUM_VERSION);
        desiredCapabilities.setCapability(CapabilityType.PLATFORM, SELENIUM_PLATFORM);
        desiredCapabilities.setCapability("build", "my build name");
        desiredCapabilities.setCapability("name", "my test name");

        URL sauceUrl = getSauceUrl(SAUCE_USERNAME, SAUCE_ACCESS_KEY);

        sauce = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
        driver = new RemoteWebDriver(sauceUrl, desiredCapabilities);

        System.out.println("platform name: " + driver.getCapabilities().getPlatform().name());
        System.out.println("platform majorVersion: " + driver.getCapabilities().getPlatform().getMajorVersion());
        System.out.println("platform minorVersion: " + driver.getCapabilities().getPlatform().getMinorVersion());
        System.out.println("browserName: " + driver.getCapabilities().getBrowserName());
        System.out.println("version: " + driver.getCapabilities().getVersion());

        for (String part : driver.getCapabilities().getPlatform().getPartOfOsName())
        {
            System.out.println("part: " + part);
        }

        System.out.println("map of desired capabilities");
        for (String capability : desiredCapabilities.asMap().keySet())
        {
            System.out.println("capability: " + capability + "=" + desiredCapabilities.getCapability(capability));
        }
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

        sauce.jobPassed(JOB_NAME);

    }



    public Properties loadTestProperties()
    {
        System.out.println("Loading properties from Env");

        // Get Sauce credentials from environment variables
        // (set by Jenkins Sauce OnDemand plugin)
        SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        // Get Selenium capabilities from environment variables
        // (set by Jenkins Sauce OnDemand plugin)
        SELENIUM_PLATFORM = System.getenv("SELENIUM_PLATFORM");
        SELENIUM_BROWSER = System.getenv("SELENIUM_BROWSER");
        SELENIUM_VERSION = System.getenv("SELENIUM_VERSION");
        SELENIUM_DRIVER = System.getenv("SELENIUM_DRIVER");
        SAUCE_ONDEMAND_BROWSERS = System.getenv("SAUCE_ONDEMAND_BROWSERS");

        // Get build from environment variables
        // (set by Jenkins)
        JOB_NAME = System.getenv("JOB_NAME");
        BUILD_NUMBER = System.getenv("BUILD_NUMBER");
        BUILD_NAME = JOB_NAME + "__" + BUILD_NUMBER;

        JENKINS_BUILD_NUMBER = System.getenv("JENKINS_BUILD_NUMBER");

        // Get test name from class
        TEST_NAME = this.getClass().getSimpleName() + "__" + testName.getMethodName();

        // add to properties
        Properties properties = new Properties();

        if (SAUCE_USERNAME != null) { properties.setProperty("SAUCE_USERNAME", SAUCE_USERNAME); }
        if (SAUCE_ACCESS_KEY != null) { properties.setProperty("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY); }
        if (SELENIUM_PLATFORM != null) { properties.setProperty("SELENIUM_PLATFORM", SELENIUM_PLATFORM); }
        if (SELENIUM_BROWSER != null) { properties.setProperty("SELENIUM_BROWSER", SELENIUM_BROWSER); }
        if (SELENIUM_VERSION != null) { properties.setProperty("SELENIUM_VERSION", SELENIUM_VERSION); }
        if (SELENIUM_DRIVER != null) { properties.setProperty("SELENIUM_DRIVER", SELENIUM_DRIVER); }
        if (SAUCE_ONDEMAND_BROWSERS != null) { properties.setProperty("SAUCE_ONDEMAND_BROWSERS", SAUCE_ONDEMAND_BROWSERS); }
        if (BUILD_NAME != null) { properties.setProperty("BUILD_NAME", BUILD_NAME); }
        if (TEST_NAME != null) { properties.setProperty("TEST_NAME", TEST_NAME); }
        if (JENKINS_BUILD_NUMBER != null) { properties.setProperty("JENKINS_BUILD_NUMBER", JENKINS_BUILD_NUMBER); }

        System.out.println("properties: " + properties);

        return properties;
    }

    public URL getSauceUrl(String SAUCE_USERNAME, String SAUCE_ACCESS_KEY) throws MalformedURLException
    {
        if (SAUCE_USERNAME != null)
        {
            SAUCE_URL = SAUCE_URL.replace("SAUCE_USERNAME", SAUCE_USERNAME);
        }

        if (SAUCE_ACCESS_KEY != null)
        {
            SAUCE_URL = SAUCE_URL.replace("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY);
        }

        return new URL(SAUCE_URL);
    }


    public DesiredCapabilities getDesiredCapabilities(Properties properties)
    {
        String SELENIUM_PLATFORM = properties.getProperty("SELENIUM_PLATFORM");
        String SELENIUM_BROWSER = properties.getProperty("SELENIUM_BROWSER");
        String SELENIUM_VERSION = properties.getProperty("SELENIUM_VERSION");

        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        if (SELENIUM_BROWSER != null)
        {
            desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        }

        if (SELENIUM_VERSION != null)
        {
            desiredCapabilities.setVersion(SELENIUM_VERSION);
        }

        if (SELENIUM_PLATFORM != null)
        {
            desiredCapabilities.setCapability(CapabilityType.PLATFORM, SELENIUM_PLATFORM);
        }

        return desiredCapabilities;
    }
}

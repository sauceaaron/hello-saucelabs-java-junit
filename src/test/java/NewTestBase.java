import com.saucelabs.saucerest.SauceREST;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class NewTestBase
{
    @Rule
    public TestName testName = new TestName();

    protected URL remoteURL;
    protected DesiredCapabilities desiredCapabilities;
    protected RemoteWebDriver web;
    protected SauceREST api;

    protected String SAUCE_USERNAME;
    protected String SAUCE_ACCESS_KEY;
    protected String SAUCE_URL = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";
    protected String REMOTE_WEBDRIVER_URL = "http://localhost:4444/wd/hub";

    protected String SELENIUM_PLATFORM;
    protected String SELENIUM_BROWSER;
    protected String SELENIUM_VERSION;

    protected String SELENIUM_DRIVER;
    protected String SAUCE_ONDEMAND_BROWSERS;

    protected String JOB_NAME;
    protected String BUILD_NUMBER;
    protected String BUILD_NAME;
    protected String JENKINS_BUILD_NUMBER;

    protected String SAUCE_JOB_IDENTIFIER;

    protected String TEST_NAME;

    protected Boolean USE_SAUCE_LABS = true;

    @Before
    public void before() throws Exception
    {
        loadEnvSettings();
    }

    @After
    public void after()
    {
        printSauceJobIdentifier();
        if (web != null)
        {
            web.quit();
        }
    }

    /**
     * Load settings from the environment
     *
     */
    private void loadEnvSettings()
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

        // Get job name and build number from environment variables
        // (set by Jenkins Sauce OnDemand plugin)
        JOB_NAME = System.getenv("JOB_NAME");
        BUILD_NUMBER = System.getenv("BUILD_NUMBER");
        JENKINS_BUILD_NUMBER = System.getenv("JENKINS_BUILD_NUMBER");

        System.out.println("JENKINS_BUILD_NUMBER: " + JENKINS_BUILD_NUMBER);

        BUILD_NAME = JOB_NAME + "__" + BUILD_NUMBER;

        // Get test name from class
        TEST_NAME = this.getClass().getSimpleName() + "__" + testName.getMethodName();
    }

    /**
     * Get the URL to connect to a RemoteWebDriver
     *
     * @param USE_SAUCE_LABS
     * @return
     * @throws MalformedURLException
     */
    public URL getRemoteWebDriverURL(Boolean USE_SAUCE_LABS) throws MalformedURLException
    {
        if (USE_SAUCE_LABS)
        {
            System.out.println("USE SAUCE LABS!!!");
            remoteURL = getSauceUrl();
        }
        else
        {
            remoteURL = new URL(REMOTE_WEBDRIVER_URL);
        }

        return remoteURL;
    }

    /**
     * Get the URL to connect to a RemoteWebDriver
     *
     * expects USE_SAUCE_LABS to be set
     *
     * @return
     * @throws MalformedURLException
     */
    public URL getRemoteWebDriverURL() throws MalformedURLException
    {
        return getRemoteWebDriverURL(USE_SAUCE_LABS);
    }

    /**
     * Build the URL used to connect to Sauce Labs
     *
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Build the URL used to connect to Sauce Labs
     *
     * expects SAUCE_USERNAME, SAUCE_ACCESS_KEY to be set
     * or to be specified in SAUCE_URL
     *
     * @return
     * @throws MalformedURLException
     */
    public URL getSauceUrl() throws MalformedURLException
    {
        return getSauceUrl(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
    }


    /**
     * Build the DesiredCapabilities
     *
     * Expects SELENIUM_PLATFORM, SELENIUM_BROWSER, SELENIUM_VERSION to be set
     *
     * @return
     */
    public DesiredCapabilities getDesiredCapabilities()
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        if (SELENIUM_PLATFORM != null)
        {
            System.out.println("SELENIUM_PLATFORM: " + SELENIUM_PLATFORM);
            desiredCapabilities.setCapability(CapabilityType.PLATFORM, SELENIUM_PLATFORM);
        }

        if (SELENIUM_BROWSER != null)
        {
            desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        }

        if (SELENIUM_VERSION != null)
        {
            desiredCapabilities.setVersion(SELENIUM_VERSION);
        }

//        if (BUILD_NAME != null)
//        {
//            desiredCapabilities.setCapability("build", BUILD_NAME);
//        }
//
//        if (TEST_NAME != null)
//        {
//            desiredCapabilities.setCapability("name", TEST_NAME);
//        }

        return desiredCapabilities;
    }

    /**
     * Get a RemoteWebDriver instance
     *
     * @param remoteURL
     * @param desiredCapabilities
     * @return
     * @throws Exception
     */
    public RemoteWebDriver getRemoteWebDriver(URL remoteURL, DesiredCapabilities desiredCapabilities) throws Exception
    {
        System.out.println("remoteURL: " + remoteURL);
        System.out.println("desiredCapabilities: " + desiredCapabilities);
        printDesiredCapabilities(desiredCapabilities);

        if (remoteURL == null)
        {
            throw new Exception("remoteURL must be set");
        }

        if (desiredCapabilities == null)
        {
            throw new Exception("desiredCapabilities must be set");
        }

        RemoteWebDriver driver = new RemoteWebDriver(remoteURL, desiredCapabilities);
        return driver;
    }

    /**
     * Get a RemoteWebDriver instance
     *
     * expects remoteURL, desiredCapabilities to be set
     *
     * @return
     * @throws Exception
     */
    public RemoteWebDriver getRemoteWebDriver() throws Exception
    {
        return getRemoteWebDriver(remoteURL, desiredCapabilities);
    }

    /**
     * Get an instance of the Sauce Labs REST API client
     *
     * @param SAUCE_USERNAME
     * @param SAUCE_ACCESS_KEY
     * @return
     */
    public SauceREST getSauceAPIClient(String SAUCE_USERNAME, String SAUCE_ACCESS_KEY)
    {
        SauceREST api = new SauceREST(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
        return api;
    }

    /**
     * Get an instance of the Sauce Labs REST API client
     *
     * Expects SAUCE_USERNAME, SAUCE_ACCESS_KEY to be set
     *
     * @return
     */
    public SauceREST getSauceAPIClient()
    {
        return getSauceAPIClient(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
    }

    /**
     * Print the SauceOnDemandSessionId and job-name to STDOUT
     * so the SauceOnDemand plugin can recognize the job
     */
    public void printSauceJobIdentifier()
    {
        SAUCE_JOB_IDENTIFIER = String.format("SauceOnDemandSessionId=%s job-name=%s", web.getSessionId(), BUILD_NAME);
        System.out.println(SAUCE_JOB_IDENTIFIER);
    }

    public void printCapabilities(Capabilities capabilities)
    {
        System.out.println("Capabilities");

        System.out.println("browserName: " + web.getCapabilities().getBrowserName());
        System.out.println("version: " + web.getCapabilities().getVersion());
        System.out.println("platform name: " + capabilities.getPlatform().name());
        System.out.println("platform majorVersion: " + web.getCapabilities().getPlatform().getMajorVersion());
        System.out.println("platform minorVersion: " + web.getCapabilities().getPlatform().getMinorVersion());

        for (String part : web.getCapabilities().getPlatform().getPartOfOsName())
        {
            System.out.println("part: " + part);
        }
    }

    public void printDesiredCapabilities(DesiredCapabilities desiredCapabilities)
    {
        System.out.println("DesiredCapabilities");

        for (String capability : desiredCapabilities.asMap().keySet())
        {
            System.out.println("capability: " + capability + "=" + desiredCapabilities.getCapability(capability));
        }
    }
}

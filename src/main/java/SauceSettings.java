import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class SauceSettings
{
    protected static final String SauceUrlString = "http://SAUCE_USERNAME:SAUCE_ACCESS_KEY@ondemand.saucelabs.com:80/wd/hub";

    public String SAUCE_USERNAME;
    public String SAUCE_ACCESS_KEY;

    public String SELENIUM_PLATFORM;
    public String SELENIUM_BROWSER;
    public String SELENIUM_VERSION;

    public String SELENIUM_DRIVER;
    public String SAUCE_ONDEMAND_BROWSERS;


    public static Properties PropertiesFromEnv()
    {
        System.out.println("Loading properties from Env");

        // Get Sauce credentials from environment variables
        // (set by Sauce OnDemand plugin)
        String SAUCE_USERNAME = System.getenv("SAUCE_USERNAME");
        String SAUCE_ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");

        // Get Selenium capabilities from environment variables
        // (set by Sauce OnDemand plugin)
        String SELENIUM_PLATFORM = System.getenv("SELENIUM_PLATFORM");
        String SELENIUM_BROWSER = System.getenv("SELENIUM_BROWSER");
        String SELENIUM_VERSION = System.getenv("SELENIUM_VERSION");

        // These are aggregates that are not strictly needed
        String SELENIUM_DRIVER = System.getenv("SELENIUM_DRIVER");
        String SAUCE_ONDEMAND_BROWSERS = System.getenv("SAUCE_ONDEMAND_BROWSERS");

        // add to properties
        Properties properties = new Properties();

        if (SAUCE_USERNAME != null) { properties.setProperty("SAUCE_USERNAME", SAUCE_USERNAME); }
        if (SAUCE_ACCESS_KEY != null) { properties.setProperty("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY); }
        if (SELENIUM_PLATFORM != null) { properties.setProperty("SELENIUM_PLATFORM", SELENIUM_PLATFORM); }
        if (SELENIUM_BROWSER != null) { properties.setProperty("SELENIUM_BROWSER", SELENIUM_BROWSER); }
        if (SELENIUM_VERSION != null) { properties.setProperty("SELENIUM_VERSION", SELENIUM_VERSION); }
        if (SELENIUM_DRIVER != null) { properties.setProperty("SELENIUM_DRIVER", SELENIUM_DRIVER); }
        if (SAUCE_ONDEMAND_BROWSERS != null) { properties.setProperty("SAUCE_ONDEMAND_BROWSERS", SAUCE_ONDEMAND_BROWSERS); }

        System.out.println("properties: " + properties);

        return properties;
    }

    public static SauceSettings FromEnv()
    {
        Properties properties = PropertiesFromEnv();
        return FromProperties(properties);
    }

    public static SauceSettings FromProperties(Properties properties)
    {
        return new SauceSettings(properties);
    }


    public static URL getSauceUrl(String SAUCE_USERNAME, String SAUCE_ACCESS_KEY) throws MalformedURLException
    {
        String urlString = SauceUrlString
                .replace("SAUCE_USERNAME", SAUCE_USERNAME)
                .replace("SAUCE_ACCESS_KEY", SAUCE_ACCESS_KEY);

        return new URL(urlString);
    }


    public SauceSettings()
    {
        Properties properties = PropertiesFromEnv();
        initFromProperties(properties);
    }

    public SauceSettings(Properties properties)
    {
        initFromProperties(properties);
    }

    private void initFromProperties(Properties properties)
    {
        SAUCE_USERNAME = properties.getProperty("SAUCE_USERNAME");
        SAUCE_ACCESS_KEY = properties.getProperty("SAUCE_ACCESS_KEY");
        SELENIUM_PLATFORM = properties.getProperty("SELENIUM_PLATFORM");
        SELENIUM_BROWSER = properties.getProperty("SELENIUM_BROWSER");
        SELENIUM_VERSION = properties.getProperty("SELENIUM_VERSION");
        SELENIUM_DRIVER = properties.getProperty("SELENIUM_DRIVER");
        SAUCE_ONDEMAND_BROWSERS = properties.getProperty("SAUCE_ONDEMAND_BROWSERS");
    }

    public URL getSauceUrl() throws MalformedURLException
    {
        if (SAUCE_USERNAME == null) { throw new RuntimeException("SAUCE_USERNAME is not set"); }
        if (SAUCE_ACCESS_KEY == null) { throw new RuntimeException(("SAUCE_ACCESS_KEY is not set")); }

        return getSauceUrl(SAUCE_USERNAME, SAUCE_ACCESS_KEY);
    }

    public DesiredCapabilities getDesiredCapabilities()
    {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        if (SELENIUM_PLATFORM != null)
        {
            desiredCapabilities.setCapability(CapabilityType.PLATFORM, SELENIUM_PLATFORM);
        }

        if (SELENIUM_BROWSER != null)
        {
            desiredCapabilities.setBrowserName(SELENIUM_BROWSER);
        }

        if (SELENIUM_VERSION != null) {
            desiredCapabilities.setVersion(SELENIUM_VERSION);
        }

        return desiredCapabilities;
    }
}

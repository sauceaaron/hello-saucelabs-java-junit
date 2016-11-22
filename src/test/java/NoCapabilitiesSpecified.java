import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static junit.framework.TestCase.assertEquals;

public class NoCapabilitiesSpecified extends SauceTestBase
{
    @Test
    public void open_sauce_labs_home_page() throws Exception
    {
        System.out.println("running test...");

        driver.get(SauceLabs.HomePage.url);
        assertEquals(SauceLabs.HomePage.Expected.title, driver.getTitle());
    }
}

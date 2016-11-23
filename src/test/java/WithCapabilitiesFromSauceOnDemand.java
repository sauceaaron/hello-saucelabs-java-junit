import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class WithCapabilitiesFromSauceOnDemand extends SauceTestBase
{
    @Test
    public void open_sauce_labs_home_page() throws Exception
    {
        System.out.println("running test...");

        driver.get(SauceLabs.HomePage.url);

        String title = driver.getTitle();
        System.out.println("got title: " + title);

        assertEquals(SauceLabs.HomePage.Expected.title, title);


        String SELENIUM_HOST = System.getenv("SELENIUM_HOST");
        String SELENIUM_PORT = System.getenv("SELENIUM_PORT");
        String SELENIUM_URL = System.getenv("SELENIUM_URL");
        String SELENIUM_STARTING_URL = System.getenv("SELENIUM_STARTING_URL");

        System.out.println("SELENIUM_HOST: " + SELENIUM_HOST);
        System.out.println("SELENIUM_PORT: " + SELENIUM_PORT);
        System.out.println("SELENIUM_URL: " + SELENIUM_URL);
        System.out.println("SELENIUM_STARTING_URL: " + SELENIUM_STARTING_URL);
    }
}

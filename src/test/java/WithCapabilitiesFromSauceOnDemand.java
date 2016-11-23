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
    }
}

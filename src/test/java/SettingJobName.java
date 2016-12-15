import org.junit.Before;
import org.junit.Test;

public class SettingJobName extends NewTestBase
{
    @Test
    public void should_set_job_name_from_jenkins() throws Exception
    {
        SELENIUM_BROWSER = "Chrome";
        SELENIUM_VERSION = "48";
        SELENIUM_PLATFORM = "Mac";

        desiredCapabilities = getDesiredCapabilities();
        printDesiredCapabilities(desiredCapabilities);

        remoteURL = getRemoteWebDriverURL();
        System.out.println("remoteURL: " + remoteURL);

        web = getRemoteWebDriver(remoteURL, desiredCapabilities);
        api = getSauceAPIClient();


        SAUCE_JOB_IDENTIFIER = String.format("SauceOnDemandSessionId=%s job-name=%s", web.getSessionId(), BUILD_NAME);
        System.out.println(SAUCE_JOB_IDENTIFIER);

        web.get("https://saucelabs.com");

        String title = web.getTitle();

        api.jobPassed(BUILD_NAME);
    }
}

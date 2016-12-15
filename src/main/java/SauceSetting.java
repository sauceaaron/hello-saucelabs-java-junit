public enum SauceSetting {
    SAUCE_USERNAME("SAUCE_USERNAME"),
    SAUCE_ACCESS_KEY("SAUCE_ACCESS_KEY"),
    SELENIUM_PLATFORM("SELENIUM_PLATFORM"),
    SELENIUM_BROWSER("SELENIUM_BROWSER"),
    SELENIUM_VERSION("SELENIUM_VERSION"),
    SELENIUM_DRIVER("SELENIUM_DRIVER"),
    SAUCE_ONDEMAND_BROWSERS("SAUCE_ONDEMAND_BROWSER");

    private String name;

    SauceSetting() {}
    SauceSetting(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }
}
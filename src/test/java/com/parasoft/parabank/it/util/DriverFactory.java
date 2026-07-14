package com.parasoft.parabank.it.util;

import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.parasoft.coverage.integration.selenium.SeleniumCoverageIntegration;
import com.parasoft.coverage.integration.selenium.SeleniumCoverageIntegration.FirefoxBrowserCoverage;
import com.parasoft.coverage.integration.selenium.SeleniumCoverageIntegration.SafariBrowserCoverage;

public final class DriverFactory {

    static {
        Logger logger = Logger.getLogger("org.openqa.selenium"); //$NON-NLS-1$
        logger.setLevel(Level.ALL);
        logger.addHandler(new SLF4JBridgeHandler());
    }

    private DriverFactory() {
    }

    public static CoverageWebDriver getDriver(String browserType) {
        String browser = browserType.trim();
        if (browser.equalsIgnoreCase("Firefox")) { //$NON-NLS-1$
            FirefoxOptions options = new FirefoxOptions();
            if (GraphicsEnvironment.isHeadless()) {
                options.addArguments("-headless"); //$NON-NLS-1$
            }
            GeckoDriverService service = null;
            File snapGeckoDriver = new File("/snap/bin/geckodriver") { //$NON-NLS-1$
                // https://github.com/SeleniumHQ/selenium/issues/7788
                // https://github.com/SeleniumHQ/selenium/issues/12495
                @Override
                public String getCanonicalPath() throws IOException {
                    return getAbsolutePath();
                }
            };
            if (snapGeckoDriver.exists()) {
                service = new GeckoDriverService.Builder().usingDriverExecutable(snapGeckoDriver).build();
            }
            FirefoxBrowserCoverage coverage = SeleniumCoverageIntegration.createFirefoxBrowserCoverage(options);
            WebDriver driver = service != null ? new FirefoxDriver(service, coverage.getFirefoxOptions())
                    : new FirefoxDriver(coverage.getFirefoxOptions());
            return new CoverageWebDriver(driver, coverage);
        }
        if (browser.equalsIgnoreCase("Edge")) { //$NON-NLS-1$
            EdgeOptions options = new EdgeOptions();
            if (GraphicsEnvironment.isHeadless()) {
                options.addArguments("--headless"); //$NON-NLS-1$
            }
            EdgeDriver driver = new EdgeDriver(options);
            SeleniumCoverageIntegration.configureCdpBaggageHeader(driver);
            return new CoverageWebDriver(driver, null);
        }
        if (browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")) { //$NON-NLS-1$ //$NON-NLS-2$
            return new CoverageWebDriver(new InternetExplorerDriver(), null);
        }
        if (browser.equalsIgnoreCase("Safari")) { //$NON-NLS-1$
            SafariBrowserCoverage coverage = SeleniumCoverageIntegration.createSafariBrowserCoverage(new SafariOptions());
            return new CoverageWebDriver(new SafariDriver(coverage.getSafariOptions()), coverage);
        }
        ChromeOptions options = new ChromeOptions();
        if (GraphicsEnvironment.isHeadless()) {
            options.addArguments("--headless=new"); //$NON-NLS-1$
            options.addArguments("--no-sandbox"); //$NON-NLS-1$
            options.addArguments("--disable-dev-shm-usage"); //$NON-NLS-1$
            options.addArguments("--window-size=1920,1080"); //$NON-NLS-1$
        }
        ChromeDriver driver = new ChromeDriver(options);
        SeleniumCoverageIntegration.configureCdpBaggageHeader(driver);
        return new CoverageWebDriver(driver, null);
    }

    public static final class CoverageWebDriver implements AutoCloseable {
        private final WebDriver driver;
        private final AutoCloseable coverage;

        private CoverageWebDriver(WebDriver driver, AutoCloseable coverage) {
            this.driver = driver;
            this.coverage = coverage;
        }

        public WebDriver getDriver() {
            return driver;
        }

        @Override
        public void close() {
            try {
                driver.quit();
            } finally {
                if (coverage != null) {
                    try {
                        coverage.close();
                    } catch (Exception e) {
                        throw new IllegalStateException("Unable to close Selenium coverage integration", e);
                    }
                }
            }
        }
    }
}

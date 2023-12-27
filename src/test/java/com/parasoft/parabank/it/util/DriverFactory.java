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
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class DriverFactory {

    static {
        Logger logger = Logger.getLogger("org.openqa.selenium"); //$NON-NLS-1$
        logger.setLevel(Level.ALL);
        logger.addHandler(new SLF4JBridgeHandler());
    }

    private DriverFactory() {
    }

    public static WebDriver getDriver(String browserType) {
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
            return service != null ? new FirefoxDriver(service, options) : new FirefoxDriver(options);
        }
        if (browser.equalsIgnoreCase("Edge")) { //$NON-NLS-1$
            EdgeOptions options = new EdgeOptions();
            if (GraphicsEnvironment.isHeadless()) {
                options.addArguments("--headless"); //$NON-NLS-1$
            }
            return new EdgeDriver(options);
        }
        if (browser.equalsIgnoreCase("IE") || browser.equalsIgnoreCase("Internet Explorer")) { //$NON-NLS-1$ //$NON-NLS-2$
            return new InternetExplorerDriver();
        }
        if (browser.equalsIgnoreCase("Safari")) { //$NON-NLS-1$
            return new SafariDriver();
        }
        ChromeOptions options = new ChromeOptions();
        if (GraphicsEnvironment.isHeadless()) {
            options.addArguments("--headless"); //$NON-NLS-1$
        }
        return new ChromeDriver(options);
    }
}

package dev.creator54.seleniumhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * This class provides configuration options for Selenium WebDriver.
 */
public class SeleniumHelperOptions {
	private int timeoutInSeconds;
	private boolean screenshotsState;
	private static final Logger logger = LogManager.getLogger(SeleniumHelperOptions.class);
	private FirefoxOptions firefoxOptions;

	/**
	 * Constructor initializes default values and sets FirefoxOptions based on the browser mode.
	 */
	public SeleniumHelperOptions() {
		// Default values for timeout and screenshot capability
		this.timeoutInSeconds = 10; // Default timeout
		this.screenshotsState = true; // Screenshot capability enabled by default

		// Initialize FirefoxOptions based on browser mode from JSON configuration
		initializeFirefoxOptions();
	}

	/**
	 * Initializes FirefoxOptions based on the configured browser mode.
	 */
	private void initializeFirefoxOptions() {
		// Default browser mode
		String browserMode = "default";

		try {
			// Attempt to obtain browser mode value
			browserMode = new JSONHelper().getValue("browser-mode");
		} catch (Exception e) {
			logger.info("browser-mode not set in config. Starting browser in non-headless mode");
		}

		// Configure FirefoxOptions based on the browser mode
		this.firefoxOptions = new FirefoxOptions().setLogLevel(FirefoxDriverLogLevel.ERROR);
		if ("headless".equals(browserMode)) {
			this.firefoxOptions.addArguments("--headless");
		}
		// No else branch needed as the non-headless mode is covered by the initial configuration and default setting
	}


	// Getter and setter for timeoutInSeconds
	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	public void setTimeoutInSeconds(int timeoutInSeconds) {
		if (this.timeoutInSeconds != timeoutInSeconds) {
			logger.info("Changing timeoutInSeconds from {} to {} seconds", this.timeoutInSeconds, timeoutInSeconds);
			this.timeoutInSeconds = timeoutInSeconds;
		}
	}

	// Getter and setter for screenshotsState
	public boolean getScreenshotsState() {
		return screenshotsState;
	}

	public void setScreenshotsState(boolean screenshotsState) {
		if (this.screenshotsState != screenshotsState) {
			logger.info("Changing screenshotsState from {} to {}", this.screenshotsState, screenshotsState);
			this.screenshotsState = screenshotsState;
		}
	}

	// Getter and setter for FirefoxOptions
	public FirefoxOptions getFirefoxOptions() {
		return firefoxOptions;
	}

	public void setFirefoxOptions(FirefoxOptions options) {
		if (options != null) {
			logger.debug("Updating FirefoxOptions. New options: {}", options);
			this.firefoxOptions = new FirefoxOptions().merge(options);
		}
	}
}

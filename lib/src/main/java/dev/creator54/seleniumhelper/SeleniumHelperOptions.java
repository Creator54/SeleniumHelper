package dev.creator54.seleniumhelper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumHelperOptions {
	private int timeoutInSeconds;
	private boolean screenshotsState;
	private static final Logger logger = LogManager.getLogger(SeleniumHelperOptions.class);
	private FirefoxOptions firefoxOptions;

	public SeleniumHelperOptions() {
		// Set default values
		this.timeoutInSeconds = 10;
		this.screenshotsState = true;
		this.firefoxOptions = new FirefoxOptions().setLogLevel(FirefoxDriverLogLevel.ERROR);
	}

	public int getTimeoutInSeconds() {
		return timeoutInSeconds;
	}

	public void setTimeoutInSeconds(int timeoutInSeconds) {
		if (this.timeoutInSeconds != timeoutInSeconds) {
			logger.info("Changing timeoutInSeconds from {} to {} seconds", this.timeoutInSeconds, timeoutInSeconds);
			this.timeoutInSeconds = timeoutInSeconds;
		}
	}

	public boolean getScreenshotsState() {
		return screenshotsState;
	}

	public void setScreenshotsState(boolean screenshotsState) {
		if (this.screenshotsState != screenshotsState) {
			logger.info("Changing screenshotsState from {} to {}", this.screenshotsState, screenshotsState);
			this.screenshotsState = screenshotsState;
		}
	}

	public FirefoxOptions getFirefoxOptions() {
		return firefoxOptions;
	}

	public void setFirefoxOptions(FirefoxOptions options) {
		// Assuming FirefoxOptions.toString() is overridden to provide meaningful output
		logger.debug("Updating FirefoxOptions. New options: {}", options);
		this.firefoxOptions = new FirefoxOptions().merge(options);
	}

}

package dev.creator54.seleniumhelper;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.devtools.v121.indexeddb.model.Key;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SeleniumHelper {
	private static final Logger logger = LogManager.getLogger(SeleniumHelper.class);
	private static WebDriver driver;
	private static WebDriverWait wait;
	private static SeleniumHelper instance;
	private static final String SCREENSHOTS_DIR = "screenshots" + File.separator;
	private int stepCount = 0;
	private final SeleniumHelperOptions options;
	private static long startTime;

	private void logActionStart(String action) {
		action = action.trim();
		logger.info("STARTING ACTION: {}", action);
	}

	private void logActionSuccess(String action) {
		action = action.trim();
		logger.info("ACTION SUCCESS: {}", action);
		if (options.getScreenshotsState() && driver != null) {
			takeScreenshot(action + " - SUCCESS");
		}
	}

	private void logActionFailure(String action) {
		// Trim the action string
		action = action.trim();

		// Log the error
		logger.error("ACTION FAILED: {}", action);

		// Attempt to take a screenshot if conditions are met
		if (options.getScreenshotsState() && driver != null) {
			try {
				takeScreenshot(action + " - FAILED");
			} catch (Exception e) {
				logger.error("Error while taking screenshot");
			}
		}

		// Quit the browser
		quit();

		// Print total time spent
		logger.info("Total time spent: {} seconds", (System.nanoTime() - startTime) / 1000000000.0);

		// Stop further execution by throwing an exception
		throw new RuntimeException();
	}

	private SeleniumHelper(SeleniumHelperOptions options) {
		this.options = options;
		logger.info("Initializing SeleniumHelper with options.");
		setup();
	}

	public static synchronized SeleniumHelper getInstance(SeleniumHelperOptions options) {
		logger.info("Requesting SeleniumHelper instance with specific options.");
		if (instance == null) {
			instance = new SeleniumHelper(options);
			logger.info("SeleniumHelper instance created with provided options.");
		} else {
			logger.info("SeleniumHelper instance already exists. Returning existing instance.");
		}
		return instance;
	}

	public static synchronized SeleniumHelper getInstance() {
		logger.info("Requesting SeleniumHelper instance with default options.");
		if (instance == null) {
			instance = new SeleniumHelper(new SeleniumHelperOptions());
			logger.info("SeleniumHelper instance created with default options.");
		} else {
			logger.info("SeleniumHelper instance already exists. Returning existing instance.");
		}
		return instance;
	}

	private void setup() {
		final String action = "Setting up WebDriver";
		logActionStart(action); // Announce the start of the setup action

		if (driver != null) {
			logger.info("WebDriver is already initialized. Skipping setup.");
			return; // Early return if the driver is already set up
		}

		try {
			FirefoxOptions firefoxOptions = options.getFirefoxOptions();
			driver = new FirefoxDriver(firefoxOptions);
			wait = new WebDriverWait(driver, Duration.ofSeconds(options.getTimeoutInSeconds()));
		} catch (Exception e) {
			logActionFailure(action); // Log other exceptions
			throw e; // Rethrow to allow higher-level handlers to catch
		}

		// Save start time
		startTime = System.nanoTime();
	}

	public static void quit() {
		final String action = "Quitting WebDriver";
		try {
			logger.info("STARTING ACTION: {}", action);
			if (driver != null) {
				driver.quit();
				logger.info("ACTION SUCCESS: {}", action);
			} else {
				logger.warn("WebDriver instance is null. No action taken.");
			}
		} catch (Exception e) {
			logger.error("ACTION FAILED: {} - Error: {}", action, e.getMessage(), e);
		} finally {
			driver = null;
		}
	}

	public boolean get(String url) {
		String action = "Navigating to URL: " + url;
		logActionStart(action); // Log the start of the navigation action
		try {
			// Check if the current URL is different to avoid unnecessary navigation
			if (!driver.getCurrentUrl().equals(url)) {
				driver.get(url);
				logActionSuccess(action); // Log successful navigation
			} else {
				logger.info("{} - URL already loaded. No action taken.", action);
			}
			return true;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to navigate to the URL
			return false;
		}
	}

	public WebElement findElement(By locator) {
		String action = "Finding element: " + locator;
		logActionStart(action); // Log the start of the find element action

		try {
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			logActionSuccess(action); // Log successful find element
			return element;
		} catch (Exception e) {
			logActionFailure(action); // Log the failure to find the element
			throw e; // Rethrow the exception to ensure the calling code can handle it or fail
			// appropriately
		}
	}

	public void waitUntilPageIsFullyLoaded() {
		final String action = "Waiting for page to load";
		logActionStart(action); // Log the start of the page load waiting action

		try {
			wait.until(
					webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
			logActionSuccess(action); // Log the successful completion of the wait
		} catch (Exception e) {
			logActionFailure(action); // Log the failure to wait for the page to load
			throw e; // Rethrow the exception to ensure that calling code is aware of the failure
		}
	}

	public List<WebElement> findElements(By locator) {
		final String action = "Finding elements: " + locator;
		logActionStart(action); // Announce the start of the action to find elements

		try {
			List<WebElement> elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
			logActionSuccess(action); // Log the successful find of elements
			return elements;
		} catch (Exception e) {
			logActionFailure(action); // Log the failure to find elements, including exception details
			throw e; // Rethrow the exception for handling elsewhere or to fail the test/action
		}
	}

	public void sendKeysToElement(By locator, String value) {
		final String action = "Sending keys to element: " + locator + ", value: " + value;
		logActionStart(action); // Log the start of the action
		String actualValue = "";
		String newValue = "";

		try {
			WebElement element = findElement(locator);

			element.sendKeys(value);

			// Re-find the element to ensure the value is actually sent to the element
			element = findElement(locator);
			actualValue = element.getAttribute("value");

			newValue = value.trim(); // Remove any leading or trailing whitespace from the value
			actualValue = actualValue.trim(); // Remove any leading or trailing whitespace from the actual value

			if (actualValue.equals(newValue)) {
				logActionSuccess(action); // Log the successful completion of the action
				return; // Exit the method if value was successfully set
			}

			// Log and throw exception if the value wasn't set after maximum retries
			throw new RuntimeException("Failed to send keys to element: " + locator + ". " +
					"Expected value: '" + newValue + "', Actual value: '" + actualValue + "'");
		} catch (Exception e) {
			// Log the failure to send keys to the element
			String failureMessage = action + " - Keys were sent but the value wasn't set as expected. " +
					"Expected value: '" + newValue + "', Actual value: '" + actualValue + "'";
			logActionFailure(failureMessage);
			throw e; // Rethrow the exception to maintain the error flow
		}
	}

	public void sendKeysToElement(By locator, Keys keys) {
		final String action = "Sending keys to element: " + locator + ", value: " + String.valueOf(keys);
		logActionStart(action); // Log the start of the action
		String actualValue = "";
		String newValue = "";

		try {
			WebElement element = findElement(locator);

			element.sendKeys(keys);

			logActionSuccess(action); // Log the successful completion of the action
		} catch (Exception e) {
			logActionFailure(action);
			throw e; // Rethrow the exception to maintain the error flow
		}
	}

	public void clickElement(By locator) {
		final String action = "Clicking element: " + locator;
		logActionStart(action); // Log the start of an action

		try {
			WebElement element = findElement(locator); // This already includes logging on success or failure
			element.click();
			logActionSuccess(action); // Log the successful completion of the action
		} catch (Exception e) {
			logActionFailure(action); // Log the failure and details of the exception

			// Throw a new exception with both the specific action and the original
			// exception for context
			throw new RuntimeException("Failed to click element: " + locator + ". " + e.getMessage(), e);
		}
	}

	public void clickElement(WebElement element) {
		final String action = "Clicking on a specified web element";
		logActionStart(action); // Announce the start of the action

		try {
			// Scroll the element into view before clicking
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
			element.click();
			logActionSuccess(action); // Log successful element click
		} catch (Exception e) {
			logActionFailure(action); // Log failure to click the element
			throw e; // Rethrow the exception for further handling
		}
	}

	public String getPageTitle() {
		final String action = "Getting the page title";
		logActionStart(action); // Log the start of the action

		try {
			String title = driver.getTitle();
			logActionSuccess(action); // Log the successful retrieval of the page title
			return title;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to retrieve the page title
			return null; // Return null to indicate failure, consistent with your original design
		}
	}

	public String getCurrentWindowHandle() {
		final String action = "Getting the current window handle";
		logActionStart(action); // Log the start of the action

		try {
			String handle = driver.getWindowHandle();
			logActionSuccess(action); // Log successful retrieval of the window handle
			return handle;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to retrieve the window handle
			return null; // Return null to indicate failure, consistent with the original approach
		}
	}

	public Set<String> getAllWindowHandles() {
		final String action = "Getting all window handles";
		logActionStart(action); // Announce the start of the action

		try {
			Set<String> handles = driver.getWindowHandles();
			logActionSuccess(action); // Log the successful retrieval of window handles
			return handles;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to retrieve window handles
			return null; // Return null to indicate failure
		}
	}

	public String getCurrentURL() {
		final String action = "Getting current URL";
		logActionStart(action); // Log the start of the action

		try {
			String url = driver.getCurrentUrl();
			logActionSuccess(action); // Log successful URL retrieval
			return url;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to retrieve the current URL
			return null; // Return null to indicate failure
		}
	}

	public String getPageSource() {
		final String action = "Getting page source";
		logActionStart(action); // Log the start of the action

		try {
			String pageSource = driver.getPageSource();
			logActionSuccess(action); // Log successful page source retrieval
			return pageSource;
		} catch (Exception e) {
			logActionFailure(action); // Log failure to retrieve the page source
			return null; // Return null to indicate failure
		}
	}

	public void switchToWindow(String handle) {
		final String action = "Switching to window with handle: " + handle;
		logActionStart(action); // Announce the start of the action

		try {
			driver.switchTo().window(handle);
			logActionSuccess(action); // Log the successful switch
		} catch (Exception e) {
			logActionFailure(action); // Log the failure to switch windows
			throw e; // Rethrow the exception for consistent error handling
		}
	}

	public void switchToFrame(WebElement frameElement) {
		final String action = "Switching to frame: " + frameElement;
		logActionStart(action); // Log the start of the action

		try {
			driver.switchTo().frame(frameElement);
			logActionSuccess(action); // Log successful frame switch
		} catch (Exception e) {
			logActionFailure(action); // Log failure to switch to the frame
			throw e; // Rethrow the exception to maintain error flow
		}
	}

	public void switchToDefaultContent() {
		final String action = "Switching to default content";
		logActionStart(action); // Log the start of the action

		try {
			driver.switchTo().defaultContent();
			logActionSuccess(action); // Log the successful return to default content
		} catch (Exception e) {
			logActionFailure(action); // Log failure to switch back to default content
			throw e; // Rethrow the exception for further handling
		}
	}

	public String takeScreenshot(String action) {
		if (!options.getScreenshotsState()) {
			logger.info("Screenshot capturing is disabled for action: {}", action);
			return null; // Early exit if screenshot capturing is disabled
		}

		File screenshotsDir = new File(SCREENSHOTS_DIR);
		if (!screenshotsDir.exists() && !screenshotsDir.mkdirs()) {
			logger.error("Failed to create screenshots directory: {}", SCREENSHOTS_DIR);
			return null; // Early exit if unable to create the directory
		}

		String screenshotName = generateScreenshotName(action);
		File screenshotFile = new File(screenshotsDir, screenshotName);
		try {
			File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screenshot, screenshotFile);
			logger.info("Screenshot saved: {}", screenshotFile.getAbsolutePath());
			return screenshotFile.getAbsolutePath();
		} catch (Exception e) {
			logActionFailure("Taking screenshot for action: " + action); // Utilize structured logging for failures
			return null;
		}
	}

	private String generateScreenshotName(String action) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		String timestamp = dateFormat.format(new Date());
		// Normalize the action string to ensure it is filesystem-safe
		String actionNormalized = action.replaceAll("[^a-zA-Z0-9\\-_]+", "_")
				.replaceAll("_+", "_")
				.trim();
		return String.format("Step_%d_%s_%s.png", stepCount++, actionNormalized, timestamp);
	}

	// get text from locator
	public String getText(By locator) {
		try {
			return findElement(locator).getText();
		} catch (Exception e) {
			return "NOT FOUND";
		}
	}

	// get text from Element
	public String getText(WebElement element) {
		try {
			return element.getText();
		} catch (Exception e) {
			return "NOT FOUND";
		}
	}
}

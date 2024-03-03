package dev.creator54.seleniumhelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class JSONHelper {
	private static String LOCATORS_FILE_PATH = "config.json";
	private static final Logger logger = LogManager.getLogger(JSONHelper.class);

	public JSONHelper() {
	}

	public JSONHelper(String filePath) {
		LOCATORS_FILE_PATH = filePath;
	}

	public String getValue(String name) {
		JSONParser parser = new JSONParser();

		try (FileReader reader = new FileReader(LOCATORS_FILE_PATH)) {
			JSONObject jsonObject = (JSONObject) parser.parse(reader);
			ArrayList<String> parts = new ArrayList<>(List.of(name.split("#")));
			String lastPart = parts.get(parts.size() - 1);

			JSONObject nestedObject = traverseJsonObject(parts, jsonObject);

			if (nestedObject != null) {
				if (nestedObject.containsKey(lastPart)) {
					Object value = nestedObject.get(lastPart);

					// Check if the value is a string
					if (value instanceof String stringValue) {
						logger.info("Retrieved value for JSONPath '{}': {}", name, stringValue);
						return stringValue;
					} else {
						logger.error("JSON path '{}' does not resolve to a valid string value", name);
						throw new IllegalArgumentException("JSON path does not resolve to a valid string value");
					}
				} else {
					logger.error("Locator not found for JSON path: '{}'", name);
					throw new IllegalArgumentException("Locator not found for JSON path: " + name);
				}
			}

			logger.error("Invalid JSON path: '{}'", name);
			throw new IllegalArgumentException("Invalid JSON path: " + name);
		} catch (IOException | ParseException e) {
			logger.error("Error reading JSON file: {}", e.getMessage());
			throw new RuntimeException(e);
		}
	}
	public By get(String name) {
		String locatorType = getValue(name + "#type");
		String locatorValue = getValue(name + "#locator");

		return switch (locatorType.toLowerCase()) {
			case "id" -> {
				logger.info("Retrieved locator of type 'id' for JSON path '{}'", name);
				yield By.id(locatorValue);
			}
			case "name" -> {
				logger.info("Retrieved locator of type 'name' for JSON path '{}'", name);
				yield By.name(locatorValue);
			}
			case "xpath" -> {
				logger.info("Retrieved locator of type 'xpath' for JSON path '{}'", name);
				yield By.xpath(locatorValue);
			}
			case "css" -> {
				logger.info("Retrieved locator of type 'css' for JSON path '{}'", name);
				yield By.cssSelector(locatorValue);
			}
			case "linktext" -> {
				logger.info("Retrieved locator of type 'linktext' for JSON path '{}'", name);
				yield By.linkText(locatorValue);
			}
			case "partiallinktext" -> {
				logger.info("Retrieved locator of type 'partiallinktext' for JSON path '{}'", name);
				yield By.partialLinkText(locatorValue);
			}
			case "tagname" -> {
				logger.info("Retrieved locator of type 'tagname' for JSON path '{}'", name);
				yield By.tagName(locatorValue);
			}
			case "classname" -> {
				logger.info("Retrieved locator of type 'classname' for JSON path '{}'", name);
				yield By.className(locatorValue);
			}
			default -> {
				logger.error("Invalid locator type: '{}'", locatorType);
				throw new IllegalArgumentException("Invalid locator type: " + locatorType);
			}
		};
	}

	public WebElement getElement(String name) {
		String locatorType = getValue(name + "#type");
		String locatorValue = getValue(name + "#locator");

		SeleniumHelper seleniumHelper = SeleniumHelper.getInstance();
		By locator = switch (locatorType.toLowerCase()) {
			case "id" -> {
				logger.info("Retrieved locator of type 'id' for JSON path '{}'", name);
				yield By.id(locatorValue);
			}
			case "name" -> {
				logger.info("Retrieved locator of type 'name' for JSON path '{}'", name);
				yield By.name(locatorValue);
			}
			case "xpath" -> {
				logger.info("Retrieved locator of type 'xpath' for JSON path '{}'", name);
				yield By.xpath(locatorValue);
			}
			case "css" -> {
				logger.info("Retrieved locator of type 'css' for JSON path '{}'", name);
				yield By.cssSelector(locatorValue);
			}
			case "linktext" -> {
				logger.info("Retrieved locator of type 'linktext' for JSON path '{}'", name);
				yield By.linkText(locatorValue);
			}
			case "partiallinktext" -> {
				logger.info("Retrieved locator of type 'partiallinktext' for JSON path '{}'", name);
				yield By.partialLinkText(locatorValue);
			}
			case "tagname" -> {
				logger.info("Retrieved locator of type 'tagname' for JSON path '{}'", name);
				yield By.tagName(locatorValue);
			}
			case "classname" -> {
				logger.info("Retrieved locator of type 'classname' for JSON path '{}'", name);
				yield By.className(locatorValue);
			}
			default -> {
				logger.error("Invalid locator type: '{}'", locatorType);
				throw new IllegalArgumentException("Invalid locator type: " + locatorType);
			}
		};

		return seleniumHelper.findElement(locator);
	}

	public List<WebElement> getElements(String name) {
		String locatorType = getValue(name + "#type");
		String locatorValue = getValue(name + "#locator");

		SeleniumHelper seleniumHelper = SeleniumHelper.getInstance();
		By locator = switch (locatorType.toLowerCase()) {
			case "id" -> {
				logger.info("Retrieved locator of type 'id' for JSON path '{}'", name);
				yield By.id(locatorValue);
			}
			case "name" -> {
				logger.info("Retrieved locator of type 'name' for JSON path '{}'", name);
				yield By.name(locatorValue);
			}
			case "xpath" -> {
				logger.info("Retrieved locator of type 'xpath' for JSON path '{}'", name);
				yield By.xpath(locatorValue);
			}
			case "css" -> {
				logger.info("Retrieved locator of type 'css' for JSON path '{}'", name);
				yield By.cssSelector(locatorValue);
			}
			case "linktext" -> {
				logger.info("Retrieved locator of type 'linktext' for JSON path '{}'", name);
				yield By.linkText(locatorValue);
			}
			case "partiallinktext" -> {
				logger.info("Retrieved locator of type 'partiallinktext' for JSON path '{}'", name);
				yield By.partialLinkText(locatorValue);
			}
			case "tagname" -> {
				logger.info("Retrieved locator of type 'tagname' for JSON path '{}'", name);
				yield By.tagName(locatorValue);
			}
			case "classname" -> {
				logger.info("Retrieved locator of type 'classname' for JSON path '{}'", name);
				yield By.className(locatorValue);
			}
			default -> {
				logger.error("Invalid locator type: '{}'", locatorType);
				throw new IllegalArgumentException("Invalid locator type: " + locatorType);
			}
		};

		return seleniumHelper.findElements(locator);
	}

	private JSONObject traverseJsonObject(ArrayList<String> parts, JSONObject jsonObject) {
		JSONObject currentObject = jsonObject;
		for (int i = 0; i < parts.size() - 1; i++) {
			Object value = currentObject.get(parts.get(i));
			if (!(value instanceof JSONObject)) {
				logger.error("Invalid JSON path: {}", String.join("#", parts));
				throw new IllegalArgumentException("Invalid JSON path: " + String.join("#", parts));
			}
			currentObject = (JSONObject) value;
		}
		return currentObject;
	}
}
